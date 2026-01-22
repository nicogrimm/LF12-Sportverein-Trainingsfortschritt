import { get, writable } from "svelte/store";

type Alert = {
  level: "info" | "error";
  title: string;
  description?: string;
};

export const alerts = writable<Alert[]>([]);

export function addAlert(alertInfo: Alert) {
  console.info("adding alert", alertInfo);
  alerts.update((list) => {
    list.push(alertInfo);
    return list;
  });
}

export function clearAlerts() {
  if (get(alerts).length != 0) {
    console.info("clearing alerts");
  }
  alerts.set([]);
}
