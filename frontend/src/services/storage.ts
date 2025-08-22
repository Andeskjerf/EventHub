export const STORAGE_KEYS = {
	USERNAME: "username",
};

export const getUsername = () => localStorage.getItem(STORAGE_KEYS.USERNAME);
export const setUsername = (username: string) =>
	localStorage.setItem(STORAGE_KEYS.USERNAME, username);
export const clearAuth = () => {
	localStorage.removeItem(STORAGE_KEYS.USERNAME);
};
export const isAuthenticated = () => !!getUsername();
