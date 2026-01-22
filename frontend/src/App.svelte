<script lang="ts">
import Loading from "$lib/components/Loading.svelte";
import { location, type Location } from "$lib/location";
  import type { Component } from "svelte";
import "./app.css";

async function loadPage(location: Location) {
  let Page;
  switch (location.page) {
    case "athletes":
      Page = await import("./pages/Athletes.svelte");
      break;
    case "athlete-details":
      Page = await import("./pages/AthleteDetails.svelte");
      break;
    case "test2":
      Page = await import("./pages/Test2.svelte");
      break;
  }
  return Page.default;
}

$inspect($location).with(console.log);
let Page: Promise<Component> = $state(new Promise(() => {}));
$effect(() => {
  Page = loadPage($location);
});
</script>

{#await Page}
  <Loading />
{:then Page}
  <Page />
{/await}
