<script lang="ts">
import * as Tabs from "$lib/components/ui/tabs/index.js";
import { location, switchLocation, type Location } from "$lib/location";

type Page = Extract<Location["page"], "sports" | "athletes" | "test2">;
</script>

<Tabs.Root
  value={$location.page}
  onValueChange={(page) => {
  switchLocation({ page: page as Page })
}}
>
  <Tabs.List>
    <Tabs.Trigger value="athletes">Athleten</Tabs.Trigger>
    <Tabs.Trigger value="sports">Sportarten</Tabs.Trigger>
    <Tabs.Trigger value="test2">Test Seite</Tabs.Trigger>
  </Tabs.List>
</Tabs.Root>
