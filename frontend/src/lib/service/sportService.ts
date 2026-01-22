import { baseUrl } from "./config";

export type Sport = {
    id: number;
    name: string;
    unit: string;
};

class SportService {
    async getSports(): Promise<Sport[]> {
        return await fetch(`${baseUrl}/api/sports`).then((resp) => resp.json());
    }

    async getSportById(id: number): Promise<Sport> {
        return await fetch(`${baseUrl}/api/sports/${id}`).then((resp) =>
            resp.json(),
        );
    }

    async createSport(sport: Omit<Sport, "id">): Promise<Sport> {
        return await fetch(`${baseUrl}/api/sports`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(sport),
        }).then((resp) => resp.json());
    }

    async updateSport(sport: Sport): Promise<Sport> {
        return await fetch(`${baseUrl}/api/sports/${sport.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(sport),
        }).then((resp) => resp.json());
    }

    async deleteSport(id: number): Promise<void> {
        await fetch(`${baseUrl}/api/sports/${id}`, {
            method: "DELETE",
        });
    }
}

export const sportService = new SportService();
