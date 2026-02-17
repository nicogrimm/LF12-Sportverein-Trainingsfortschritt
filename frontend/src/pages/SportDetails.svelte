<script lang="ts">
import Loading from "$lib/components/Loading.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import Chart from "$lib/components/Chart.svelte";
import { addAlert, clearAlerts } from "$lib/alerts";
import { location } from "$lib/location";
import Button, {
  buttonVariants,
} from "$lib/components/ui/button/button.svelte";
import * as AlertDialog from "$lib/components/ui/alert-dialog";
import { sportService, type Sport } from "$lib/service/sportService";
import { trainingService, type Training } from "$lib/service/trainingService";
import { athleteService, type Athlete } from "$lib/service/athleteService";
import { getErrorMessage } from "$lib/service/fetchUtils";
import { Input } from "$lib/components/ui/input/index.js";
import * as Dialog from "$lib/components/ui/dialog";
import { Label } from "$lib/components/ui/label";
import { Checkbox } from "$lib/components/ui/checkbox";
import TrainingsTable from "$lib/components/TrainingsTable.svelte";
import type { ChartData } from "chart.js";

let data: Sport | undefined = $state();
let promise = $state(loadData());

let editDialogOpened = $state(false);
let editSportFormRef: HTMLFormElement | null = $state(null);
let deleteDialogOpen = $state(false);

type AthleteDataset = {
  athlete: Athlete;
  trainings: Training[];
  selected: boolean;
  color: string;
};

let athleteDatasets: AthleteDataset[] = $state([]);

let chartData: ChartData = $derived({
  labels: getUniqueDates(),
  datasets: athleteDatasets
    .filter((ad) => ad.selected)
    .map((ad) => ({
      label: `${ad.athlete.firstname} ${ad.athlete.name}`,
      data: getUniqueDates().map((date) => {
        const t = ad.trainings.find(
          (tr) => new Date(tr.date).toLocaleDateString("de-DE") === date,
        );
        return t ? t.metric : null;
      }),
      fill: false,
      borderColor: ad.color,
      spanGaps: true,
    })),
});

function getUniqueDates(): string[] {
  const allDates = new Set<string>();
  for (const ad of athleteDatasets) {
    for (const t of ad.trainings) {
      allDates.add(new Date(t.date).toLocaleDateString("de-DE"));
    }
  }
  return [...allDates].sort((a, b) => {
    const [da, ma, ya] = a.split(".").map(Number);
    const [db, mb, yb] = b.split(".").map(Number);
    return new Date(ya, ma - 1, da).getTime() - new Date(yb, mb - 1, db).getTime();
  });
}

async function loadData() {
  try {
    if ($location.page !== "sport-details") {
      console.error("bad page state");
      return;
    }

    return await sportService
      .getSportById($location.sportId)
      .then((sport) => {
        data = sport;
        return loadAthleteCharts(sport.id).then(() => sport);
      });
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Holen der Sportart-Details",
      description: getErrorMessage(err),
    });
  }
}

async function refreshData() {
  promise = loadData();
}

async function loadAthleteCharts(sportId: number) {
  try {
    const trainings = await trainingService.getTrainingsForSport(sportId);

    const byAthlete = new Map<number, Training[]>();
    for (const t of trainings) {
      if (!byAthlete.has(t.athleteId)) byAthlete.set(t.athleteId, []);
      byAthlete.get(t.athleteId)!.push(t);
    }

    const datasets: AthleteDataset[] = [];
    let i = 0;
    for (const [athleteId, athleteTrainings] of byAthlete) {
      const athlete = await athleteService.getAthleteById(athleteId);
      const sorted = athleteTrainings.sort(
        (a, b) => new Date(a.date).getTime() - new Date(b.date).getTime(),
      );
      datasets.push({
        athlete,
        trainings: sorted,
        selected: true,
        color: `hsl(${(i * 137) % 360}, 70%, 50%)`,
      });
      i++;
    }

    athleteDatasets = datasets;
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Laden der Diagramm-Daten",
      description: getErrorMessage(err),
    });
  }
}

async function handleDelete() {
  try {
    await sportService.deleteSport(data!.id);
    window.history.back();
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Löschen der Sportart",
      description: getErrorMessage(err),
    });
  }
  deleteDialogOpen = false;
}

async function submitUpdate(event: SubmitEvent) {
  event.preventDefault();
  if (!editSportFormRef) {
    return;
  }

  clearAlerts();

  let formData = new FormData(editSportFormRef);
  try {
    await sportService.updateSport({
      id: data!.id,
      name: formData.get("name")!.toString(),
      unit: formData.get("unit")!.toString(),
    });
  } catch (e) {
    console.error(e);
    addAlert({
      level: "error",
      title: "Fehler beim Ändern der Daten",
      description: getErrorMessage(e),
    });
    return;
  }

  editDialogOpened = false;
  refreshData();
}
</script>

<main class="flex w-full flex-col items-start">
  {#await promise}
    <Loading />
  {:then}
    <div class="flex w-full flex-col gap-2">
      <Button class="w-fit" onclick={() => window.history.back()}>Zurück</Button
      >

      {#if !editDialogOpened}
        <Alertbox />
      {/if}

      <div class="wrap flex flex-col gap-4">
        <div class="flex w-fit flex-col gap-2">
          <span class="font-bold">Id</span>
          <span class="w-full border border-slate-700"></span>
          <span>{data?.id}</span>
        </div>
        <div class="flex w-fit flex-col gap-2">
          <span class="font-bold">Name</span>
          <span class="w-full border border-slate-700"></span>
          <span>{data?.name}</span>
        </div>
        <div class="flex w-fit flex-col gap-2">
          <span class="font-bold">Einheit</span>
          <span class="w-full border border-slate-700"></span>
          <span>{data?.unit}</span>
        </div>
      </div>

      <div class="flex-row gap-2">
        <Dialog.Root bind:open={editDialogOpened}>
          <Dialog.Trigger
            class={buttonVariants({ variant: "default" })}
            aria-label="bearbeiten"
          >
            Bearbeiten
          </Dialog.Trigger>
          <Dialog.Content class="sm:max-w-[425px]">
            <Dialog.Header>
              <Dialog.Title>Sportart bearbeiten</Dialog.Title>
            </Dialog.Header>
            <form
              class="grid gap-4"
              bind:this={editSportFormRef}
              onsubmit={submitUpdate}
            >
              <Alertbox />
              <div class="grid gap-4">
                <div class="grid gap-3">
                  <Label for="name-1">Name</Label>
                  <Input id="name-1" name="name" value={data?.name} />
                </div>
                <div class="grid gap-3">
                  <Label for="unit-1">Einheit</Label>
                  <Input id="unit-1" name="unit" value={data?.unit} />
                </div>
              </div>
              <Dialog.Footer>
                <Dialog.Close
                  class={buttonVariants({ variant: "outline" })}
                  type="button">Abbrechen</Dialog.Close
                >
                <Button type="submit">Speichern</Button>
              </Dialog.Footer>
            </form>
          </Dialog.Content>
        </Dialog.Root>

        <AlertDialog.Root bind:open={deleteDialogOpen}>
          <AlertDialog.Trigger
            class={buttonVariants({ variant: "destructive" })}
          >
            Löschen
          </AlertDialog.Trigger>
          <AlertDialog.Content>
            <AlertDialog.Header>
              <AlertDialog.Title>Bist du dir sicher?</AlertDialog.Title>
              <AlertDialog.Description>
                Diese Aktion kann nicht rückgängig gemacht werden.
              </AlertDialog.Description>
            </AlertDialog.Header>
            <AlertDialog.Footer>
              <AlertDialog.Cancel>Abbrechen</AlertDialog.Cancel>
              <AlertDialog.Action
                onclick={handleDelete}
                class={buttonVariants({ variant: "destructive" })}
                >Weiter</AlertDialog.Action
              >
            </AlertDialog.Footer>
          </AlertDialog.Content>
        </AlertDialog.Root>
      </div>
    </div>

    {#if data?.id}
      <h3 class="mt-8 text-lg font-bold">Trainings</h3>
      <TrainingsTable variant="sport" parentId={data.id} dialogOpened={false} onchange={() => loadAthleteCharts(data!.id)} />

      {#if athleteDatasets.length > 0}
        <h3 class="mt-8 text-lg font-bold">Trainingsfortschritt</h3>
        <div class="flex flex-wrap gap-4 rounded border p-3">
          {#each athleteDatasets as ad, i}
            <label class="flex items-center gap-2 text-sm">
              <Checkbox
                checked={ad.selected}
                onCheckedChange={(checked) => (athleteDatasets[i].selected = checked === true)}
              />
              <span >{ad.athlete.firstname} {ad.athlete.name}</span>
            </label>
          {/each}
        </div>
        <div class="h-96 w-full rounded border p-2">
          <Chart data={chartData} />
        </div>
      {/if}
    {/if}
  {/await}
</main>
