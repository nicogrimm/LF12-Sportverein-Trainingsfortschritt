<script lang="ts">
  import { onMount, onDestroy } from "svelte";
  import { SvelteMap } from "svelte/reactivity";
  import {
    Chart,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    LineController,
    BarElement,
    BarController,
    ArcElement,
    RadialLinearScale,
    PieController,
    DoughnutController,
    RadarController,
    Title,
    Tooltip,
    Legend,
    Filler,
  } from "chart.js";
  import type { ChartData, ChartType, ChartOptions } from "chart.js";

  Chart.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    LineController,
    BarElement,
    BarController,
    ArcElement,
    RadialLinearScale,
    PieController,
    DoughnutController,
    RadarController,
    Title,
    Tooltip,
    Legend,
    Filler,
  );

  interface Props {
    data: ChartData;
    type?: ChartType;
    options?: ChartOptions;
  }

  const defaultOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    elements: {
      point: {
        radius: 4,
        hoverRadius: 8,
        pointStyle: "circle",
        backgroundColor: "white",
      },
    },
    plugins: {
      legend: {
        display: true,
        onClick: () => {},
        labels: {
          usePointStyle: true,
        },
      },
    },
    transitions: {
      show: {
        animations: {
          x: { from: 0 },
          y: { from: 0 },
        },
      },
      hide: {
        animations: {
          x: { to: 0 },
          y: { to: 0 },
        },
      },
    },
  };

  let { data, type = "line", options = {} }: Props = $props();

  let canvas: HTMLCanvasElement;
  let chart: Chart | undefined;

  function mergedOptions(): ChartOptions {
    return { ...defaultOptions, ...options };
  }

  function plainData(): ChartData {
    return JSON.parse(JSON.stringify(data)) as ChartData;
  }

  onMount(() => {
    chart = new Chart(canvas, {
      type,
      data: plainData(),
      options: mergedOptions(),
    });
  });

  $effect(() => {
    if (chart) {
      const hiddenState = new SvelteMap<number, boolean>();
      chart.data.datasets.forEach((ds, i) => {
        hiddenState.set(i, chart!.isDatasetVisible(i) === false);
      });

      const newData = plainData();

      newData.datasets.forEach((ds, i) => {
        if (hiddenState.has(i)) {
          ds.hidden = hiddenState.get(i);
        }
      });

      chart.data = newData;
      chart.options = mergedOptions();
      chart.update();
    }
  });

  onDestroy(() => {
    chart?.destroy();
  });
</script>

<div class="relative h-full w-full">
  <canvas bind:this={canvas}></canvas>
</div>
