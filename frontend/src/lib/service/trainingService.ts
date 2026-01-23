import { baseUrl } from "./config";
import { fetchWithErrorHandling } from "./fetchUtils";

export type Training = {
    id: number;
    athleteId: number;
    sportId: number;
    date: string;
    metric: number;
};

class TrainingService {
    async getAllTrainings(): Promise<Training[]> {
        return await fetchWithErrorHandling(`${baseUrl}/api/trainings`).then((resp) => resp.json());
    }

    async getTrainingsForAthlete(athleteId: number): Promise<Training[]> {
        return await fetchWithErrorHandling(`${baseUrl}/api/trainings/athlete/${athleteId}`).then(
            (resp) => resp.json(),
        );
    }

    async getTrainingForAthlete(
        athleteId: number,
        trainingId: number,
    ): Promise<Training> {
        return await fetchWithErrorHandling(
            `${baseUrl}/api/trainings/athlete/${athleteId}/training/${trainingId}`,
        ).then((resp) => resp.json());
    }

    async createTrainingForAthlete(
        athleteId: number,
        training: Omit<Training, "id">,
    ): Promise<Training> {
        return await fetchWithErrorHandling(`${baseUrl}/api/trainings/athlete/${athleteId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ ...training, athleteId }),
        }).then((resp) => resp.json());
    }

    async updateTrainingForAthlete(
        athleteId: number,
        trainingId: number,
        training: Omit<Training, "id">,
    ): Promise<Training> {
        return await fetchWithErrorHandling(
            `${baseUrl}/api/trainings/athlete/${athleteId}/training/${trainingId}`,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ ...training, athleteId }),
            },
        ).then((resp) => resp.json());
    }

    async deleteTrainingForAthlete(
        athleteId: number,
        trainingId: number,
    ): Promise<void> {
        await fetchWithErrorHandling(
            `${baseUrl}/api/trainings/athlete/${athleteId}/training/${trainingId}`,
            {
                method: "DELETE",
            },
        );
    }
}

export const trainingService = new TrainingService();
