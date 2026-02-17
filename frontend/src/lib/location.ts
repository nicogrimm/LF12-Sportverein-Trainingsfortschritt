import { writable } from "svelte/store";
import { addAlert } from "./alerts";

export type Location =
  | {
    page: "athlete-details";
    athleteId: number;
  }
  | {
    page: "sport-details";
    sportId: number;
  }
  | {
    page: "training-details";
    athleteId: number;
    trainingId: number;
  }
  | {
    page: "sports" | "athletes";
  };

export const location = writable<Location>(readLocation());

// ensure that there is always an empty state object available
window.history.replaceState({}, "", window.location.href);

// handle history forward and back events
window.addEventListener("popstate", (event) => {
  if (event.state !== null) {
    reloadLocation();
  }
});

function readLocation(): Location {
  const pathParts = window.location.pathname.split("/").slice(1);

  switch (pathParts[0] ?? "") {
    case "athletes":
      if (pathParts[1]) {
        let id;
        try {
          id = parseInt(pathParts[1]);
        } catch {
          addAlert({ level: "error", title: "Id muss eine Zahl seien" });
          break;
        }

        return { page: "athlete-details", athleteId: id };
      }

      return { page: "athletes" };
    case "sports":
      if (pathParts[1]) {
        let id;
        try {
          id = parseInt(pathParts[1]);
        } catch {
          addAlert({ level: "error", title: "Id muss eine Zahl seien" });
          break;
        }

        return { page: "sport-details", sportId: id };
      }

      return { page: "sports" };
    case "training":
      if (!pathParts[2]) {
        break;
      }

      let athleteId;
      let trainingId;
      try {
        athleteId = parseInt(pathParts[1]);
        trainingId = parseInt(pathParts[2]);
      } catch {
        addAlert({ level: "error", title: "Id muss eine Zahl seien" });
        break;
      }

      return { page: "training-details", athleteId, trainingId };
  }

  return { page: "athletes" };
}

export function reloadLocation() {
  location.set(readLocation());
}

export function switchLocation(newLocation: Location) {
  location.set(newLocation);

  const url = new URL(window.location.href);
  switch (newLocation.page) {
    case "athlete-details":
      url.pathname = "/athletes/" + newLocation.athleteId;
      window.history.pushState({}, "", url.toString());
      break;
    case "athletes":
      url.pathname = "/athletes";
      window.history.pushState({}, "", url.toString());
      break;
    case "sport-details":
      url.pathname = "/sports/" + newLocation.sportId;
      window.history.pushState({}, "", url.toString());
      break;
    case "sports":
      url.pathname = "/sports";
      window.history.pushState({}, "", url.toString());
      break;
    case "training-details":
      url.pathname = "/training/" + newLocation.athleteId + "/" + newLocation.trainingId;
      window.history.pushState({}, "", url.toString());
      break;
  }
}
