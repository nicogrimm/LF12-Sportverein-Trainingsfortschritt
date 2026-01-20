<script lang="ts">
  import viteLogo from "/vite.svg";

  let testMsg: string | undefined;

  async function fetchTest() {
    let resp = await fetch("http://localhost:8080/test");
    testMsg = await resp.text();
  }
</script>

<main class="flex flex-col place-items-center min-h-screen m-w-screen m-0 bg-white text-slate-900">
  <div>
    <a href="https://vite.dev" target="_blank" rel="noreferrer">
      <img src={viteLogo} class="logo" alt="Vite Logo" />
    </a>
  </div>
  <h1>Vite + Svelte</h1>

  <p>
    Check out <a
      href="https://github.com/sveltejs/kit#readme"
      target="_blank"
      rel="noreferrer">SvelteKit</a
    >, the official Svelte app framework powered by Vite!
  </p>

  <p class="text-slate-600">Click on the Vite and Svelte logos to learn more</p>

  <div>
    <button onclick={fetchTest}>Test</button>
    {#if testMsg != undefined}
      <p>{testMsg}</p>
    {/if}
  </div>
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
