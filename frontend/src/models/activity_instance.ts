export interface ActivityInstance {
	activityId: string;
	activityInstanceId: string;
	name: string;
	eventDate: string; // ISO 8601 string from ZonedDateTime
	registerBefore: number;
	location: string;
	meetLocation: string;
	description?: string;
	participants: number;
	maxParticipants: number;
	repeatInterval: number;
}
