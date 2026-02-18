<script lang="ts">
import { trainingService, type Training } from "$lib/service/trainingService";
import { location } from "$lib/location";
import { getErrorMessage } from "$lib/service/fetchUtils";
import { addAlert, clearAlerts } from "$lib/alerts";
import Loading from "$lib/components/Loading.svelte";
import Button, {
  buttonVariants,
} from "$lib/components/ui/button/button.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import * as Dialog from "$lib/components/ui/dialog";
import * as AlertDialog from "$lib/components/ui/alert-dialog";
import { Label } from "$lib/components/ui/label";
import { Input } from "$lib/components/ui/input";
import { formatDateToInputFieldFormat } from "$lib/utils";
import TooltipSnippet from "$lib/components/TooltipSnippet.svelte";
import { athleteService, type Athlete } from "$lib/service/athleteService";
import { sportService, type Sport } from "$lib/service/sportService";

let data: Training | undefined = $state();
let athlete: Athlete | undefined = $state();
let sport: Sport | undefined = $state();
let promise = $state(loadData());

let editDialogOpened = $state(false);
let editTrainingFormRef: HTMLFormElement | null = $state(null);
let deleteDialogOpen = $state(false);

async function loadData() {
  try {
    if ($location.page !== "training-details") {
      console.error("bad page state");
      return;
    }

    const training = await trainingService.getTrainingForAthlete(
      $location.athleteId,
      $location.trainingId,
    );
    data = training;

    athlete = await athleteService.getAthleteById(data.athleteId);
    sport = await sportService.getSportById(data.sportId);

    return training;
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Holen der Training-Details",
      description: getErrorMessage(err),
    });
  }
}

function refreshData() {
  promise = loadData();
}

async function handleDelete() {
  try {
    await trainingService.deleteTrainingForAthlete(data!.athleteId, data!.id);
    window.history.back();
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Löschen des Trainings",
      description: getErrorMessage(err),
    });
  }
  deleteDialogOpen = false;
}

async function submitUpdate(event: SubmitEvent) {
  event.preventDefault();
  if (!editTrainingFormRef) {
    return;
  }

  clearAlerts();

  let formData = new FormData(editTrainingFormRef);
  try {
    await trainingService.updateTrainingForAthlete(data!.athleteId, data!.id, {
      athleteId: data!.athleteId,
      sportId: data!.sportId,
      date: new Date(formData.get("date") as string).toISOString(),
      metric: parseFloat(formData.get("metric") as string),
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
        <div class="flex w-fit flex-row gap-2">
          <div class="flex flex-col gap-2">
            <span class="font-bold">Athlet</span>
            <span class="w-full border border-slate-700"></span>
            <span
              ><TooltipSnippet
                content={`${athlete?.firstname} ${athlete?.name}`}
                tooltip={`Id: ${athlete?.id}`}
              /></span
            >
          </div>
          <div class="flex flex-col gap-2">
            <span class="font-bold">Sportart</span>
            <span class="w-full border border-slate-700"></span>
            <span
              ><TooltipSnippet
                content={`${sport?.name}`}
                tooltip={`Id: ${sport?.id}`}
              /></span
            >
          </div>
          <div class="flex flex-col gap-2">
            <span class="font-bold">Datum</span>
            <span class="w-full border border-slate-700"></span>
            <span>
              {#if data}
                {new Date(data?.date).toLocaleString()}
              {/if}
            </span>
          </div>
          <div class="flex flex-col gap-2">
            <span class="font-bold">Metrik</span>
            <span class="w-full border border-slate-700"></span>
            <span>{data?.metric} {sport?.unit}</span>
          </div>
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
              <Dialog.Title>Athleten bearbeiten</Dialog.Title>
            </Dialog.Header>
            <form
              class="grid gap-4"
              bind:this={editTrainingFormRef}
              onsubmit={submitUpdate}
            >
              <Alertbox />
              <div class="grid gap-4">
                <div class="grid gap-3">
                  <Label>Athlet</Label>
                  <TooltipSnippet
                    content={`${athlete!.firstname} ${athlete!.name}`}
                    tooltip={`Id: ${athlete!.id}`}
                  />
                </div>
                <div class="grid gap-3">
                  <Label>Sportart</Label>
                  <TooltipSnippet
                    content={sport!.name}
                    tooltip={`Id: ${sport!.id}`}
                  />
                </div>
                <div class="grid gap-3">
                  <Label for="date-1">Datum</Label>
                  <Input
                    id="date-1"
                    name="date"
                    type="datetime-local"
                    value={formatDateToInputFieldFormat(data?.date ? new Date(data.date) : new Date())}
                    required
                  />
                </div>
                <div class="grid gap-3">
                  <Label for="metric-1">Metrik ({sport?.unit})</Label>
                  <Input
                    id="metric-1"
                    name="metric"
                    type="number"
                    step="any"
                    value={data?.metric}
                    required
                  />
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
  {/await}
</main>
