import type { ActivityInstance } from "@/models/activity_instance";

export const activityUtils = {
	formatParticipationCount(activity: ActivityInstance): string {
		if (activity.maxParticipants == 0) {
			return `${activity.participants} deltakere`;
		}
		return `${activity.participants} / ${activity.maxParticipants} deltakere`;
	},

	formatEventTime(eventDate: string): string {
		const date = new Date(eventDate);
		return `${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
	},

	formatEventDate(eventDate: string): string {
		const date = new Date(eventDate);
		const weekday = date.toLocaleDateString("nb-NO", { weekday: "long" });
		const month = date.toLocaleDateString("nb-NO", { month: "long" });
		return `${weekday}, ${date.getDate()} ${month}`;
	},
};
