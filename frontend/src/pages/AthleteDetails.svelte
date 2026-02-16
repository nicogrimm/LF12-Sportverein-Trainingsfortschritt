<script lang="ts">
import { athleteService, type Athlete } from "$lib/service/athleteService";
import Loading from "$lib/components/Loading.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import { addAlert, clearAlerts } from "$lib/alerts";
import { location } from "$lib/location";
import Button, {
  buttonVariants,
} from "$lib/components/ui/button/button.svelte";
import * as AlertDialog from "$lib/components/ui/alert-dialog";
import { getErrorMessage } from "$lib/service/fetchUtils";
import { Input } from "$lib/components/ui/input/index.js";
import * as Dialog from "$lib/components/ui/dialog";
import { Label } from "$lib/components/ui/label";
  import TrainingsTable from "$lib/components/TrainingsTable.svelte";

let data: Athlete | undefined = $state();
let promise = $state(loadData());

let editDialogOpened = $state(false);
let editAthleteFormRef: HTMLFormElement | null = $state(null);
let deleteDialogOpen = $state(false);

let trainingsTablesDialogOpened = $state(false);

async function loadData() {
  try {
    if ($location.page !== "athlete-details") {
      console.error("bad page state");
      return;
    }

    return await athleteService
      .getAthleteById($location.athleteId)
      .then((athlete) => (data = athlete));
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Holen der Athleten-Details",
      description: getErrorMessage(err),
    });
  }
}

async function refreshData() {
  promise = loadData();
}

async function handleDelete() {
  try {
    await athleteService.deleteAthlete(data!.id);
    window.history.back();
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Löschen des Athleten",
      description: getErrorMessage(err),
    });
  }
  deleteDialogOpen = false;
}

async function submitUpdate(event: SubmitEvent) {
  event.preventDefault();
  if (!editAthleteFormRef) {
    return;
  }

  clearAlerts();

  let formData = new FormData(editAthleteFormRef);
  try {
    await athleteService.updateAthlete({
      id: data!.id,
      firstname: formData.get("firstname")!.toString(),
      name: formData.get("name")!.toString(),
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

      {#if !editDialogOpened && !trainingsTablesDialogOpened}
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
            <span class="font-bold">Vorname</span>
            <span class="w-full border border-slate-700"></span>
            <span>{data?.firstname}</span>
          </div>
          <div class="flex flex-col gap-2">
            <span class="font-bold">Nachname</span>
            <span class="w-full border border-slate-700"></span>
            <span>{data?.name}</span>
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
              bind:this={editAthleteFormRef}
              onsubmit={submitUpdate}
            >
              <Alertbox />
              <div class="grid gap-4">
                <div class="grid gap-3">
                  <Label for="firstname-1">Vorname</Label>
                  <Input id="firstname-1" name="firstname" value={data?.firstname} />
                </div>
                <div class="grid gap-3">
                  <Label for="name-1">Nachname</Label>
                  <Input id="name-1" name="name" value={data?.name} />
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

      {#if data?.id}
        <h3 class="mt-8 text-lg font-bold">Trainings</h3>
        <TrainingsTable variant="athlet" parentId={data.id} bind:dialogOpened={trainingsTablesDialogOpened} />
      {/if}
    </div>
  {/await}
</main>
