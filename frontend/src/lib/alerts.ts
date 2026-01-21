import { writable } from "svelte/store";

type Alert = {
  level: "info" | "error";
  title: string;
  description?: string;
};

const alerts = writable<Alert[]>([]);

export const useAlerts = () => ({
  alerts,
  addAlert(alertInfo: Alert) {
    alerts.update((list) => {
      list.push(alertInfo);
      return list;
    });
  },
});
