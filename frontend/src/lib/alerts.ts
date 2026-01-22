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
    console.info("adding alert", alertInfo);
    alerts.update((list) => {
      list.push(alertInfo);
      return list;
    });
  },
  clearAlerts() {
    console.info("clearing alerts");
    alerts.set([]);
  },
});
