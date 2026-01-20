<script lang="ts">
    import { switchPage } from "$lib/navigation";
import "./app.css";

const path = window.location.pathname;

async function loadPage() {
  switch (path) {
    case "/test1":
      const test1 = await import("./pages/Default.svelte");
      return test1.default;
    case "/test2":
      const test2 = await import("./pages/Test2.svelte");
      return test2.default;
    default:
      switchPage("test1");
  }
}
</script>

{#await loadPage() then page}
  <svelte:component this={page} />
{/await}
