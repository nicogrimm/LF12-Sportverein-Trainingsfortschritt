<script lang="ts">
import type { ComponentProps } from "svelte";
import ArrowUpDownIcon from "@lucide/svelte/icons/arrow-up-down";
import ArrowUpIcon from "@lucide/svelte/icons/arrow-up";
import ArrowDownIcon from "@lucide/svelte/icons/arrow-down";
import { Button } from "$lib/components/ui/button/index.js";
import type { SortDirection } from "@tanstack/table-core";

let {
  text,
  variant = "ghost",
  sortDir,
  ...restProps
}: ComponentProps<typeof Button> & {
  text: string;
  sortDir?: false | SortDirection;
} = $props();
</script>

<Button variant={variant} {...restProps}>
  {text}
  {#if sortDir === "asc"}
    <ArrowDownIcon class="ms-2" />
  {:else if sortDir === "desc"}
    <ArrowUpIcon class="ms-2" />
  {:else}
    <ArrowUpDownIcon class="ms-2" />
  {/if}
</Button>
