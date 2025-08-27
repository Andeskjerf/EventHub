export interface Participant {
	id: string;
	activityInstanceId: string;
	name: string;
	anonymized: boolean;
	phoneNumber: string;
	activityOptionNames: string[];
}
