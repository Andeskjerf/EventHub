export interface CreateActivityRequest {
	name: string;
	eventDate: string;
	registerBefore: number;
	location: string;
	meetupLocation: string;
	description: string;
	maxParticipants: number;
	repeatInterval: number;
}
