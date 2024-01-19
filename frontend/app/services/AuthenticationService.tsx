// stackoverflow.com/questions/49819183/react-what-is-the-best-way-to-handle-login-and-authentication
import { cookies } from "next/headers";

export async function isAuthenticated(): Promise<boolean> {
  const cookieStore = cookies();
  const authToken = cookieStore.get("auth_token")?.value;
  if (authToken === undefined) {
    return false;
  }
  return await validToken(authToken);
}

export async function validToken(jwt: string): Promise<boolean> {
  // this needs to change if the backend is located on a different url
  // sadly this can't be "api/authentication", but I dont know why
  const managementUrl =
    process.env.MANAGMENT_HOST ??
    "http://23ws-ase-pr-inso-04.apps.student.inso-w.at/management";

  const response = await fetch(`${managementUrl}/authentication`, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  return response.status === 200;
}
