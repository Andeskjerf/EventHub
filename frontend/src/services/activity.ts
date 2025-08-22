import { apiClient } from "@/api/axios";
import type { CreateActivityRequest } from "@/models/create_activity_request";

export const activityService = {
	async createActivity(activity: CreateActivityRequest) {
		const response = await apiClient.post("/activity/create", activity);
		console.log(response);
	},
};
