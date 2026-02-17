<script lang="ts">
import * as Tooltip from "$lib/components/ui/tooltip/index.js";
import { buttonVariants } from "./ui/button";

let {
  content,
  tooltip,
}: {
  content: string;
  tooltip: string;
} = $props();
</script>

<Tooltip.Provider>
  <Tooltip.Root>
    <Tooltip.Trigger class={buttonVariants({ variant: "ghost", size: "sm" })}
      >{content}</Tooltip.Trigger
    >
    <Tooltip.Content>
      <p>{tooltip}</p>
    </Tooltip.Content>
  </Tooltip.Root>
</Tooltip.Provider>
