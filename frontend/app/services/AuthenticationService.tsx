// stackoverflow.com/questions/49819183/react-what-is-the-best-way-to-handle-login-and-authentication
import { jwtVerify } from "jose";
import { cookies } from "next/headers";

export async function isAuthenticated(): Promise<boolean> {
  const cookieStore = cookies();
  const authToken = cookieStore.get("auth_token")?.value;
  if (authToken === undefined) {
    return false;
  }
  return await validToken(authToken);
}
export const getAccessToken = (): string | null => null; // null = not authenticated / a string = authenticated

const secret = Buffer.from(
  "2e0377d9c56d8a51ed8cfc10a68bda5b3cdc7453a6c7b6ac4728d93e052618051bba4534b146e3cff10bed31224793cb46f78a628b8d6dc08b1b5496a05cf488",
  "base64"
);

export async function validToken(token: string): Promise<boolean> {
  try {
    await jwtVerify(token, secret, {});
  } catch (ex) {
    return false;
  }
  return true;
}
