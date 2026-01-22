<script lang="ts">
import type { Athlete } from "$lib/service/athleteService";
import EllipsisIcon from "@lucide/svelte/icons/ellipsis";
import { Button } from "$lib/components/ui/button/index.js";
import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
import { switchLocation } from "$lib/location";

let { athlete }: { athlete: Athlete } = $props();
</script>

<div></div>

<DropdownMenu.Root>
  <DropdownMenu.Trigger>
    {#snippet child({ props })}
      <Button
        {...props}
        variant="outline"
        size="icon"
        class="relative size-8 p-0"
      >
        <span class="sr-only">Menu öffnen</span>
        <EllipsisIcon />
      </Button>
    {/snippet}
  </DropdownMenu.Trigger>
  <DropdownMenu.Content>
    <DropdownMenu.Group>
      <DropdownMenu.Label>Aktionen</DropdownMenu.Label>
    </DropdownMenu.Group>
    <DropdownMenu.Item
      onclick={() => switchLocation({ page: "athlete-details", athleteId: athlete.id })}
    >
      Zur Detailansicht gehen
    </DropdownMenu.Item>
    <DropdownMenu.Separator />
    <DropdownMenu.Item
      onclick={() => navigator.clipboard.writeText(athlete.id.toString())}
    >
      Id kopieren
    </DropdownMenu.Item>
  </DropdownMenu.Content>
</DropdownMenu.Root>
