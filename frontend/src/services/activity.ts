import { apiClient } from "@/api/axios";
import type { ActivityInstance } from "@/models/activity_instance";
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

	async getNextInstanceOfAllActiveActivities(): Promise<
		Array<ActivityInstance>
	> {
		const response = await apiClient.get("/activity/next-active");
		return response.data;
	},
};
