<script lang="ts">
import { useAlerts } from "$lib/alerts";
import Alertbox from "$lib/components/Alertbox.svelte";
import { Button } from "$lib/components/ui/button";

let testMsg: string | undefined;

async function fetchTest() {
  try {
    let resp = await fetch("http://localhost:8080/test");

    testMsg = await resp.text();
  } catch (e) {
    let { addAlert } = useAlerts();

    addAlert({
      level: "error",
      title: "Server hat eine unerwartet Antwort gegeben",
    });
  }
}
</script>

<main
  class="m-w-screen m-0 flex min-h-screen flex-col place-items-center bg-white text-slate-900"
>
  <h2>Test2 page</h2>

  <a href="/" class="underline">Go to home</a>

  <div class="gap-2">
    <Alertbox />

    <Button onclick={fetchTest}>Test</Button>

    {#if testMsg != undefined}
      <p>{testMsg}</p>
    {/if}
  </div>
</main>
