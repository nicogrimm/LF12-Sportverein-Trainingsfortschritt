import { baseUrl } from "./config";

export type Athlete = {
    id: number;
    firstname: string;
    name: string;
};

class AthleteService {
    async getAthletes(): Promise<Athlete[]> {
        return await fetch(`${baseUrl}/api/athletes`).then((resp) => resp.json());
    }

    async getAthleteById(id: number): Promise<Athlete> {
        return await fetch(`${baseUrl}/api/athletes/${id}`).then((resp) =>
            resp.json(),
        );
    }

    async createAthlete(athlete: Omit<Athlete, "id">): Promise<Athlete> {
        return await fetch(`${baseUrl}/api/athletes`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(athlete),
        }).then((resp) => resp.json());
    }

    async updateAthlete(athlete: Athlete): Promise<Athlete> {
        return await fetch(`${baseUrl}/api/athletes/${athlete.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(athlete),
        }).then((resp) => resp.json());
    }

    async deleteAthlete(id: number): Promise<void> {
        await fetch(`${baseUrl}/api/athletes/${id}`, {
            method: "DELETE",
        });
    }
}

export const athleteService = new AthleteService();
