<script lang="ts">
import ChevronDownIcon from "@lucide/svelte/icons/chevron-down";
import RefreshCwIcon from "@lucide/svelte/icons/refresh-cw";
import {
  type ColumnDef,
  type ColumnFiltersState,
  type RowSelectionState,
  type SortingState,
  type VisibilityState,
  getCoreRowModel,
  getFilteredRowModel,
  getSortedRowModel,
  type Table as TanstackTable,
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
import TrainingDataTableActions from "$lib/components/TrainingDataTableActions.svelte";
import DataTableCheckbox from "$lib/components/DataTableCheckbox.svelte";
import * as AlertDialog from "$lib/components/ui/alert-dialog";
import Loading from "$lib/components/Loading.svelte";
import Alertbox from "$lib/components/Alertbox.svelte";
import { addAlert, clearAlerts } from "$lib/alerts";
import * as Dialog from "$lib/components/ui/dialog";
import { Label } from "$lib/components/ui/label";
import { getErrorMessage } from "$lib/service/fetchUtils";
import { trainingService, type Training } from "$lib/service/trainingService";
import { sportService, type Sport } from "$lib/service/sportService";
import { formatDateToInputFieldFormat } from "$lib/utils";

let {
  variant,
  parentId,
  dialogOpened = $bindable(),
}: {
  variant: "sport" | "athlet";
  parentId: number;
  dialogOpened: boolean;
} = $props();

let data: Training[] = [];
let sports: Record<number, Sport> = {};
let promise = $state(loadData());

async function loadData() {
  try {
    let training: Training[];
    switch (variant) {
      case "sport":
        training = await trainingService.getTrainingsForSport(parentId);
      case "athlet":
        training = await trainingService.getTrainingsForAthlete(parentId);
    }
    data = training;

    for (const t of data) {
      if (sports[t.sportId] === undefined) {
        sports[t.sportId] = await sportService.getSportById(t.sportId);
      }
    }

    return data;
  } catch (err) {
    console.error(err);
    addAlert({
      level: "error",
      title: "Fehler beim Holen der Trainings",
      description: getErrorMessage(err),
    });
  }
}

async function refreshData() {
  promise = loadData();
}

let addTrainingDialogOpened = $state(false);
let addTrainingFormRef: HTMLFormElement | null = $state(null);

$effect(() => {
  dialogOpened = addTrainingDialogOpened;
  clearAlerts();
});

async function submitAdd(event: SubmitEvent) {
  event.preventDefault();
  if (!addTrainingFormRef) {
    return;
  }
  clearAlerts();

  let data = new FormData(addTrainingFormRef);
  try {
    await trainingService.createTrainingForAthlete({
      athleteId: parentId,
      sportId: parseInt(data.get("sportId")!.toString()),
      date: data.get("date") ? new Date(data.get("date")!.toString()).toISOString() : "",
      metric: parseInt(data.get("metric")!.toString()),
    });
  } catch (e) {
    console.error(e);
    addAlert({
      level: "error",
      title: "Fehler beim Erstellen des Trainings",
      description: getErrorMessage(e),
    });
    return;
  }

  addTrainingDialogOpened = false;

  refreshData();
}

async function handleDelete() {
  clearAlerts();

  for (let i = 0; i < data.length; i++) {
    if (rowSelection[i]) {
      try {
        await trainingService.deleteTrainingForAthlete(
          data[i].athleteId,
          data[i].id,
        );
      } catch (err) {
        console.error(err);
        addAlert({
          level: "error",
          title: "Fehler beim Löschen des Trainings",
          description: getErrorMessage(err),
        });
      }
    }
  }
  rowSelection = {};

  refreshData();
}

const parentCol: ColumnDef<Training> = $derived(
  variant == "sport"
    ? {
        id: "athlete",
        accessorKey: "athleteId",
        header: ({ column }) =>
          renderComponent(DataTableButton, {
            text: "Athlet",
            onclick: column.getToggleSortingHandler(),
          }),
        cell: ({ row }) => {
          const sportSnippet = createRawSnippet<[{ sportId: number }]>(
            (getSport) => {
              // TODO: Make it pretty instead of showing just an Id
              const { sportId } = getSport();
              return {
                render: () => `<div>${sportId}</div>`,
              };
            },
          );

          return renderSnippet(sportSnippet, {
            sportId: row.original.sportId,
          });
        },
      }
    : {
        id: "sport",
        accessorKey: "sportId",
        header: ({ column }) =>
          renderComponent(DataTableButton, {
            text: "Sportart",
            onclick: column.getToggleSortingHandler(),
          }),
        cell: ({ row }) => {
          const sportSnippet = createRawSnippet<[{ sportId: number }]>(
            (getSport) => {
              // TODO: Make it pretty instead of showing just an Id
              const { sportId } = getSport();
              return {
                render: () => `<div>${sportId}</div>`,
              };
            },
          );

          return renderSnippet(sportSnippet, {
            sportId: row.original.sportId,
          });
        },
      },
);

const columns: ColumnDef<Training>[] = $derived([
  {
    id: "select",
    header: ({ table }) =>
      renderComponent(DataTableCheckbox, {
        checked: table.getIsAllPageRowsSelected(),
        indeterminate:
          table.getIsSomePageRowsSelected() &&
          !table.getIsAllPageRowsSelected(),
        onCheckedChange: (value) => table.toggleAllPageRowsSelected(!!value),
        "aria-label": "Select all",
      }),
    cell: ({ row }) =>
      renderComponent(DataTableCheckbox, {
        checked: row.getIsSelected(),
        onCheckedChange: (value) => row.toggleSelected(!!value),
        "aria-label": "Select row",
      }),
    enableSorting: false,
    enableHiding: false,
  },
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
  parentCol,
  {
    id: "metrik",
    accessorKey: "metric",
    header: ({ column }) =>
      renderComponent(DataTableButton, {
        text: "Metrik",
        onclick: column.getToggleSortingHandler(),
      }),
    cell: ({ row }) => {
      const metricSnippet = createRawSnippet<
        [{ metric: number; sport: Sport }]
      >((getData) => {
        const { metric, sport } = getData();
        return {
          render: () => `<div>${metric} ${sport.unit}</div>`,
        };
      });

      return renderSnippet(metricSnippet, {
        metric: row.original.metric,
        sport: sports[row.original.sportId],
      });
    },
  },
  {
    id: "datum",
    accessorKey: "date",
    header: ({ column }) =>
      renderComponent(DataTableButton, {
        text: "Datum",
        onclick: column.getToggleSortingHandler(),
      }),
    cell: ({ row }) => {
      const dateSnippet = createRawSnippet<[{ date: string }]>((getDate) => {
        const { date } = getDate();
        const formattedDate = new Date(date).toLocaleString();
        return {
          render: () => `<div>${formattedDate}</div>`,
        };
      });

      return renderSnippet(dateSnippet, {
        date: row.original.date,
      });
    },
  },
  {
    id: "actions",
    enableHiding: false,
    cell: ({ row }) =>
      renderComponent(TrainingDataTableActions, { training: row.original }),
  },
]);

// let pagination = $state<PaginationState>({ pageIndex: 0, pageSize: 10 });
let sorting = $state<SortingState>([]);
let columnFilters = $state<ColumnFiltersState>([]);
let rowSelection = $state<RowSelectionState>({});
let columnVisibility = $state<VisibilityState>({});

// TODO: grouping by athlet/sport (order by athletid/sportid and then date)
let table: TanstackTable<Training> | undefined = $state();
$effect(() => {
  table = createSvelteTable({
    get data() {
      return data;
    },
    columns,
    state: {
      // get pagination() {
      //   return pagination;
      // },
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
    // getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    // onPaginationChange: (updater) => {
    //   if (typeof updater === "function") {
    //     pagination = updater(pagination);
    //   } else {
    //     pagination = updater;
    //   }
    // },
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
});
</script>

<main class="flex w-full flex-col items-start">
  {#if !addTrainingDialogOpened}
    <Alertbox />
  {/if}

  {#await promise}
    <Loading />
  {:then}
    <div class="-mb-8 w-full">
      <div class="flex items-center gap-4 py-4">
        <!--      <Input -->
        <!--        placeholder="Filter nach Namen..." -->
        <!--        value={(table?.getColumn("name")?.getFilterValue() as string) ?? ""} -->
        <!--        oninput={(e) => -->
        <!--  table?.getColumn("name")?.setFilterValue(e.currentTarget.value)} -->
        <!--        onchange={(e) => { -->
        <!--  table?.getColumn("name")?.setFilterValue(e.currentTarget.value); -->
        <!-- }} -->
        <!--        class="max-w-sm" -->
        <!--      /> -->

        {#if variant == "athlet"}
          <Dialog.Root bind:open={addTrainingDialogOpened}>
            <Dialog.Trigger
              class={buttonVariants({ variant: "default" })}
              aria-label="Neuen Training hinzufügen"
            >
              +
            </Dialog.Trigger>
            <Dialog.Content class="sm:max-w-[425px]">
              <Dialog.Header>
                <Dialog.Title>Training hinzufügen</Dialog.Title>
              </Dialog.Header>
              <form
                class="grid gap-4"
                bind:this={addTrainingFormRef}
                onsubmit={submitAdd}
              >
                <Alertbox />
                <div class="grid gap-4">
                  <!-- private int trainingId; -->
                  <!-- private int athleteId; -->
                  <!-- private int sportId; -->
                  <!-- private OffsetDateTime date; -->
                  <!-- private Float metric; -->
                  <div class="grid gap-3">
                    <!-- TODO: show name instead of id -->
                    <Label for="firstname-1">Athlet</Label>
                    <Input
                      id="firstname-1"
                      name="athleteId"
                      type="number"
                      value={parentId}
                      disabled
                    />
                  </div>
                  <div class="grid gap-3">
                    <!-- TODO: select from existing data and show resulting unit -->
                    <Label for="name-1">Sportart</Label>
                    <Input id="name-1" name="sportId" type="number" required />
                  </div>
                  <div class="grid gap-3">
                    <Label for="name-1">Datum</Label>
                    <Input
                      id="name-1"
                      name="date"
                      type="datetime-local"
                      required
                      value={formatDateToInputFieldFormat(new Date())}
                    />
                  </div>
                  <div class="grid gap-3">
                    <Label for="name-1">Metrik</Label>
                    <Input
                      id="name-1"
                      name="metric"
                      type="number"
                      step="any"
                      required
                    />
                  </div>
                </div>
                <Dialog.Footer>
                  <Dialog.Close
                    class={buttonVariants({ variant: "outline" })}
                    type="button">Abbrechen</Dialog.Close
                  >
                  <Button type="submit">Hinzufügen</Button>
                </Dialog.Footer>
              </form>
            </Dialog.Content>
          </Dialog.Root>
        {/if}
        <Button onclick={refreshData}><RefreshCwIcon /></Button>
        <DropdownMenu.Root>
          <DropdownMenu.Trigger>
            {#snippet child({ props })}
              <Button {...props} variant="outline" class="ms-auto">
                Spalten <ChevronDownIcon class="ms-2 size-4" />
              </Button>
            {/snippet}
          </DropdownMenu.Trigger>
          <DropdownMenu.Content align="end">
            {#each table?.getAllColumns()
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
            {#each table?.getHeaderGroups() as headerGroup (headerGroup.id)}
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
            {#each table?.getRowModel().rows as row (row.id)}
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
      <!-- <div class="flex items-center justify-end space-x-2 pt-4"> -->
      <!-- <div class="flex-1 text-sm text-muted-foreground"> -->
      <!--   {table.getFilteredSelectedRowModel().rows.length} von -->
      <!--   {table.getFilteredRowModel().rows.length} Zeile(n) ausgewählt. -->
      <!-- </div> -->
      <!-- <div class="space-x-2"> -->
      <!--   <Button -->
      <!--     variant="outline" -->
      <!--     size="sm" -->
      <!--     onclick={() => table.previousPage()} -->
      <!--     disabled={!table.getCanPreviousPage()} -->
      <!--   > -->
      <!--     Zurück -->
      <!--   </Button> -->
      <!--   <Button -->
      <!--     variant="outline" -->
      <!--     size="sm" -->
      <!--     onclick={() => table.nextPage()} -->
      <!--     disabled={!table.getCanNextPage()} -->
      <!--   > -->
      <!--     Weiter -->
      <!--   </Button> -->
      <!-- </div> -->
      <!-- </div> -->
      <div class="flex items-center justify-end space-x-2 pt-4">
        <AlertDialog.Root>
          <AlertDialog.Trigger
            class={buttonVariants({ variant: "destructive" })}
            disabled={Object.keys(rowSelection).length == 0}
          >
            Auswahl löschen
          </AlertDialog.Trigger>
          <AlertDialog.Content>
            <AlertDialog.Header>
              <AlertDialog.Title>Bist du dir sicher?</AlertDialog.Title>
              <AlertDialog.Description>
                Diese Aktion kann nicht rückgängig gemacht werden.
              </AlertDialog.Description>
            </AlertDialog.Header>
            <AlertDialog.Footer>
              <AlertDialog.Cancel>Abbrechen</AlertDialog.Cancel>
              <AlertDialog.Action
                onclick={handleDelete}
                class={buttonVariants({ variant: "destructive" })}
                >Weiter</AlertDialog.Action
              >
            </AlertDialog.Footer>
          </AlertDialog.Content>
        </AlertDialog.Root>
      </div>
    </div>
  {/await}
</main>
