export interface CreateActivityRequest {
	name: string;
	eventDate: string;
	registerBefore: number;
	location: string;
	meetLocation: string;
	description: string;
	maxParticipants: number;
	repeatInterval: number;
}
