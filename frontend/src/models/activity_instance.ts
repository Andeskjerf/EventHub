export interface ActivityInstance {
	activityId: string;
	instanceId: string;
	name: string;
	eventDate: string; // ISO 8601 string from ZonedDateTime
	registerBefore: number;
	location: string;
	meetLocation: string;
	description?: string;
	maxParticipants: number;
	repeatInterval: number;
}
