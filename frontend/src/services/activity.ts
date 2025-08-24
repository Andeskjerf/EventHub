import { apiClient } from "@/api/axios";
import type { ActivityInstance } from "@/models/activity_instance";
import type { CreateActivityRequest } from "@/models/create_activity_request";
import type { CreateParticipant } from "@/models/create_participant";

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

	async getActivityInfo(instanceId: string) {
		try {
			const response = await apiClient.get(`/activity/${instanceId}`);
			return {
				success: true,
				data: response.data,
				message: "activity gotten",
			};
		} catch (error) {
			return {
				success: false,
				data: null,
				message: error.response?.data?.error || "failed to get activity",
			};
		}
	},

	// TODO: handle errors
	async getNextInstanceOfAllActiveActivities(): Promise<
		Array<ActivityInstance>
	> {
		const response = await apiClient.get("/activity/next-active");
		return response.data;
	},

	// TODO: handle errors
	async registerParticipant(participant: CreateParticipant) {
		const response = await apiClient.post(
			`/activity/${participant.activityId}/participants`,
			participant,
		);
		console.log(response);
	},

	// TODO: handle errors
	async getParticipants(instanceId: string) {
		const response = await apiClient.get(
			`/activity/${instanceId}/participants`,
		);

		return response.data;
	},
};
