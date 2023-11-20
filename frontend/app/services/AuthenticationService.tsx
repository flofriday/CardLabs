// stackoverflow.com/questions/49819183/react-what-is-the-best-way-to-handle-login-and-authentication

export const isAuthenticated = (): boolean =>
  !((getAccessToken() ?? "") === "");
export const getAccessToken = (): string | null => "Mock"; // null = not authenticated / a string = authenticated
