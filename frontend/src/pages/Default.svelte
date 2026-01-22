<script lang="ts">
import ChevronDownIcon from "@lucide/svelte/icons/chevron-down";
import {
  type ColumnDef,
  type ColumnFiltersState,
  type PaginationState,
  type RowSelectionState,
  type SortingState,
  type VisibilityState,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
} from "@tanstack/table-core";
import { createRawSnippet } from "svelte";
import * as Table from "$lib/components/ui/table/index.js";
import { Button, buttonVariants } from "$lib/components/ui/button/index.js";
import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
import { Input } from "$lib/components/ui/input/index.js";
import {
  FlexRender,
  createSvelteTable,
  renderComponent,
  renderSnippet,
} from "$lib/components/ui/data-table/index.js";
import DataTableButton from "$lib/components/DataTableButton.svelte";
import { athleteService, type Athlete } from "$lib/service/athleteService";
import Loading from "$lib/components/Loading.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import { useAlerts } from "$lib/alerts";
import * as Dialog from "$lib/components/ui/dialog";
import { Label } from "$lib/components/ui/label";

let { addAlert, clearAlerts } = useAlerts();

let data: Athlete[] = [];
let promise = $state(loadData());

async function loadData() {
  try {
    const athletes = await athleteService.getAthletes();
    return (data = athletes);
  } catch (err) {
    console.error(err);
    addAlert({ level: "error", title: "Fehler beim Holen der Daten" });
  }
}

let addAthleteDialogOpened = $state(false);
let addAthleteFormRef: HTMLFormElement | null = $state(null);

$effect(() => {
  addAthleteDialogOpened; // run anytime the dialog is opened or closed
  clearAlerts();
});

async function submitAdd(event: SubmitEvent) {
  event.preventDefault();

  if (!addAthleteFormRef) {
    return;
  }

  let data = new FormData(event.target as HTMLFormElement);
  try {
    await athleteService.createAthlete({
      firstname: data.get("firstname")!.toString(),
      name: data.get("name")!.toString(),
    });
  } catch (e) {
    console.error(e);
    addAlert({ level: "error", title: " Fehler beim Absenden der Daten" });
  }

  addAthleteDialogOpened = false;

  // refresh list
  promise = loadData();
}

const columns: ColumnDef<Athlete>[] = [
  // {
  //  id: "select",
  //  header: ({ table }) =>
  //   renderComponent(DataTableCheckbox, {
  //    checked: table.getIsAllPageRowsSelected(),
  //    indeterminate:
  //     table.getIsSomePageRowsSelected() &&
  //     !table.getIsAllPageRowsSelected(),
  //    onCheckedChange: (value) => table.toggleAllPageRowsSelected(!!value),
  //    "aria-label": "Select all"
  //   }),
  //  cell: ({ row }) =>
  //   renderComponent(DataTableCheckbox, {
  //    checked: row.getIsSelected(),
  //    onCheckedChange: (value) => row.toggleSelected(!!value),
  //    "aria-label": "Select row"
  //   }),
  //  enableSorting: false,
  //  enableHiding: false
  // },
  {
    accessorKey: "id",
    header: ({ column }) =>
      renderComponent(DataTableButton, {
        text: "Id",
        onclick: column.getToggleSortingHandler(),
      }),
    cell: ({ row }) => {
      const idSnippet = createRawSnippet<[{ id: number }]>((getId) => {
        const { id } = getId();
        return {
          render: () => `<div>${id}</div>`,
        };
      });

      return renderSnippet(idSnippet, {
        id: row.original.id,
      });
    },
  },
  {
    id: "name",
    accessorFn: (row) => row.firstname + " " + row.name,
    header: ({ column }) =>
      renderComponent(DataTableButton, {
        text: "Name",
        onclick: column.getToggleSortingHandler(),
      }),
    cell: ({ row }) => {
      const nameSnippet = createRawSnippet<
        [{ firstname: string; name: string }]
      >((getName) => {
        const { firstname, name } = getName();
        return {
          render: () => `<div>${firstname} ${name}</div>`,
        };
      });

      return renderSnippet(nameSnippet, {
        firstname: row.original.firstname,
        name: row.original.name,
      });
    },
  },
  // {
  //  id: "actions",
  //  enableHiding: false,
  //  cell: ({ row }) =>
  //   renderComponent(DataTableActions, { id: row.original.id })
  // }
];

let pagination = $state<PaginationState>({ pageIndex: 0, pageSize: 10 });
let sorting = $state<SortingState>([]);
let columnFilters = $state<ColumnFiltersState>([]);
let rowSelection = $state<RowSelectionState>({});
let columnVisibility = $state<VisibilityState>({});

const table = createSvelteTable({
  get data() {
    return data;
  },
  columns,
  state: {
    get pagination() {
      return pagination;
    },
    get sorting() {
      return sorting;
    },
    get columnVisibility() {
      return columnVisibility;
    },
    get rowSelection() {
      return rowSelection;
    },
    get columnFilters() {
      return columnFilters;
    },
  },
  getCoreRowModel: getCoreRowModel(),
  getPaginationRowModel: getPaginationRowModel(),
  getSortedRowModel: getSortedRowModel(),
  getFilteredRowModel: getFilteredRowModel(),
  onPaginationChange: (updater) => {
    if (typeof updater === "function") {
      pagination = updater(pagination);
    } else {
      pagination = updater;
    }
  },
  onSortingChange: (updater) => {
    if (typeof updater === "function") {
      sorting = updater(sorting);
    } else {
      sorting = updater;
    }
  },
  onColumnFiltersChange: (updater) => {
    if (typeof updater === "function") {
      columnFilters = updater(columnFilters);
    } else {
      columnFilters = updater;
    }
  },
  onColumnVisibilityChange: (updater) => {
    if (typeof updater === "function") {
      columnVisibility = updater(columnVisibility);
    } else {
      columnVisibility = updater;
    }
  },
  onRowSelectionChange: (updater) => {
    if (typeof updater === "function") {
      rowSelection = updater(rowSelection);
    } else {
      rowSelection = updater;
    }
  },
});
</script>

<main class="flex w-full flex-col items-start">
  <a href="/test2" class="underline">Zur Seite: Test2</a>

  <Alertbox />

  {#await promise}
    <Loading />
  {:then}
    <div class="-mb-8 w-full">
      <div class="flex items-center gap-4 py-4">
        <Input
          placeholder="Filter nach Namen..."
          value={(table.getColumn("name")?.getFilterValue() as string) ?? ""}
          oninput={(e) =>
    table.getColumn("name")?.setFilterValue(e.currentTarget.value)}
          onchange={(e) => {
    table.getColumn("name")?.setFilterValue(e.currentTarget.value);
   }}
          class="max-w-sm"
        />

        <Dialog.Root bind:open={addAthleteDialogOpened}>
          <Dialog.Trigger
            class={buttonVariants({ variant: "default" })}
            aria-label="Neuen Athleten hinzufügen"
          >
            +
          </Dialog.Trigger>
          <Dialog.Content class="sm:max-w-[425px]">
            <Dialog.Header>
              <Dialog.Title>Athlete hinzufügen</Dialog.Title>
            </Dialog.Header>
            <Alertbox />
            <form
              class="grid gap-4"
              bind:this={addAthleteFormRef}
              onsubmit={submitAdd}
            >
              <div class="grid gap-4">
                <div class="grid gap-3">
                  <Label for="firstname-1">Vorname</Label>
                  <Input id="firstname-1" name="firstname" />
                </div>
                <div class="grid gap-3">
                  <Label for="name-1">Nachname</Label>
                  <Input id="name-1" name="name" />
                </div>
              </div>
              <Dialog.Footer>
                <Dialog.Close class={buttonVariants({ variant: "outline" })}
                  >Abbrechen</Dialog.Close
                >
                <Button type="submit">Hinzufügen</Button>
              </Dialog.Footer>
            </form>
          </Dialog.Content>
        </Dialog.Root>
        <DropdownMenu.Root>
          <DropdownMenu.Trigger>
            {#snippet child({ props })}
              <Button {...props} variant="outline" class="ms-auto">
                Spalten <ChevronDownIcon class="ms-2 size-4" />
              </Button>
            {/snippet}
          </DropdownMenu.Trigger>
          <DropdownMenu.Content align="end">
            {#each table
     .getAllColumns()
     .filter((col) => col.getCanHide()) as column (column)}
              <DropdownMenu.CheckboxItem
                class="capitalize"
                bind:checked={() => column.getIsVisible(), (v) => column.toggleVisibility(!!v)}
              >
                {column.id}
              </DropdownMenu.CheckboxItem>
            {/each}
          </DropdownMenu.Content>
        </DropdownMenu.Root>
      </div>
      <div class="rounded-md border">
        <Table.Root>
          <Table.Header>
            {#each table.getHeaderGroups() as headerGroup (headerGroup.id)}
              <Table.Row>
                {#each headerGroup.headers as header (header.id)}
                  <Table.Head class="[&:has([role=checkbox])]:ps-3">
                    {#if !header.isPlaceholder}
                      <FlexRender
                        content={header.column.columnDef.header}
                        context={header.getContext()}
                      />
                    {/if}
                  </Table.Head>
                {/each}
              </Table.Row>
            {/each}
          </Table.Header>
          <Table.Body>
            {#each table.getRowModel().rows as row (row.id)}
              <Table.Row data-state={row.getIsSelected() && "selected"}>
                {#each row.getVisibleCells() as cell (cell.id)}
                  <Table.Cell class="[&:has([role=checkbox])]:ps-3">
                    <FlexRender
                      content={cell.column.columnDef.cell}
                      context={cell.getContext()}
                    />
                  </Table.Cell>
                {/each}
              </Table.Row>
            {:else}
              <Table.Row>
                <Table.Cell colspan={columns.length} class="h-24 text-center">
                  Keine Daten vorhanden
                </Table.Cell>
              </Table.Row>
            {/each}
          </Table.Body>
        </Table.Root>
      </div>
      <div class="flex items-center justify-end space-x-2 pt-4">
        <div class="flex-1 text-sm text-muted-foreground">
          {table.getFilteredSelectedRowModel().rows.length} von
          {table.getFilteredRowModel().rows.length} Zeile(n) ausgewählt.
        </div>
        <div class="space-x-2">
          <Button
            variant="outline"
            size="sm"
            onclick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
          >
            Zurück
          </Button>
          <Button
            variant="outline"
            size="sm"
            onclick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}
          >
            Weiter
          </Button>
        </div>
      </div>
    </div>
  {/await}
</main>
