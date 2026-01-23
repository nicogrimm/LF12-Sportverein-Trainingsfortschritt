<script lang="ts">
import { athleteService, type Athlete } from "$lib/service/athleteService";
import Loading from "$lib/components/Loading.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import { addAlert } from "$lib/alerts";
import { location } from "$lib/location";
import Button, {
  buttonVariants,
} from "$lib/components/ui/button/button.svelte";
import * as AlertDialog from "$lib/components/ui/alert-dialog";
import { getErrorMessage } from "$lib/service/fetchUtils";

let data: Athlete | undefined = $state();
let promise = $state(loadData());

let deleteDialogOpen = $state(false);

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
      description: getErrorMessage(err)
    });
  }
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
      description: getErrorMessage(err)
    });
  }
  deleteDialogOpen = false;
}
</script>

<main class="flex w-full flex-col items-start">
  {#await promise}
    <Loading />
  {:then}
    <div class="flex w-full flex-col gap-2">
      <Button class="w-fit" onclick={() => window.history.back()}>Zurück</Button
      >

      <Alertbox />

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
