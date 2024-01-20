// stackoverflow.com/questions/49819183/react-what-is-the-best-way-to-handle-login-and-authentication
import { cookies } from "next/headers";

export async function isAuthenticated(): Promise<boolean> {
  console.log("isAuthenticalsdkfjha");
  const cookieStore = cookies();
  const authToken = cookieStore.get("auth_token")?.value;
  console.log(`authToken: ${authToken}`);
  if (authToken === undefined) {
    return false;
  }
  return await validToken(authToken);
}

export async function validToken(jwt: string): Promise<boolean> {
  // this needs to change if the backend is located on a different url
  // sadly this can't be "api/authentication", but I dont know why
  const response = await fetch("http://127.0.0.1:8080/authentication", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });
  console.log(response);

  if (response.status === 200) {
    console.log(response.text());
    return true;
  } else {
    return false;
  }
}
