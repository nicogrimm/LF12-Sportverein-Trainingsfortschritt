<script lang="ts">
import Loading from "$lib/components/Loading.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import { addAlert } from "$lib/alerts";
import { location } from "$lib/location";
import Button, {
  buttonVariants,
} from "$lib/components/ui/button/button.svelte";
import * as AlertDialog from "$lib/components/ui/alert-dialog";
import { sportService, type Sport } from "$lib/service/sportService";

let data: Sport | undefined = $state();
let promise = $state(loadData());

async function loadData() {
  try {
    if ($location.page !== "sport-details") {
      console.error("bad page state");
      return;
    }

    return await sportService
      .getSportById($location.sportId)
      .then((sport) => (data = sport));
  } catch (err) {
    console.error(err);
    addAlert({ level: "error", title: "Fehler beim Holen der Daten" });
  }
}

async function handleDelete() {
  await sportService.deleteSport(data!.id);
  window.history.back();
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
            <span class="font-bold">Nachname</span>
            <span class="w-full border border-slate-700"></span>
            <span>{data?.name}</span>
          </div>
          <div class="flex flex-col gap-2">
            <span class="font-bold">Einheit</span>
            <span class="w-full border border-slate-700"></span>
            <span>{data?.unit}</span>
          </div>
        </div>
      </div>

      <div class="flex-row gap-2">
        <AlertDialog.Root>
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
