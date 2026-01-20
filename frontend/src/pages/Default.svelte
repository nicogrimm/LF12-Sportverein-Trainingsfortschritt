<script lang="ts">
import viteLogo from "/vite.svg";
import Button from "$lib/components/ui/button/button.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import { useAlerts } from "$lib/alerts";

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
  <div>
    <a href="https://vite.dev" target="_blank" rel="noreferrer">
      <img src={viteLogo} class="logo" alt="Vite Logo" />
    </a>
  </div>
  <h1>Vite + Svelte</h1>
  <p>
    Check out

    <a
      href="https://github.com/sveltejs/kit#readme"
      target="_blank"
      rel="noreferrer"
    >
      SvelteKit
    </a>
    , the official Svelte app framework powered by Vite!
  </p>
  <p class="text-slate-600">Click on the Vite and Svelte logos to learn more</p>
  <div class="gap-2">
    <Alertbox />

    <Button onclick={fetchTest}>Test</Button>

    {#if testMsg != undefined}
      <p>{testMsg}</p>
    {/if}
  </div>

  <a href="/test2" class="underline">Go to test2 page</a>
</main>

<style>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}

.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
</style>
