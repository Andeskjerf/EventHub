import { apiClient } from "@/api/axios";
import type { CreateActivityRequest } from "@/models/create_activity_request";

export const activityService = {
	async createActivity(activity: CreateActivityRequest) {
		try {
			const response = await apiClient.post("/activity/create", activity);
			return {
				success: true,
				data: response.data,
				message: "activity created",
			};
		} catch (error) {
			return {
				success: false,
				data: null,
				message: error.response?.data?.error || "failed to create activity",
			};
		}
	},
};
