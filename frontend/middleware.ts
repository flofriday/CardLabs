import { validToken } from "./app/services/AuthenticationService";
import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

// protected sites
const protectedRoutes = ["/settings"];

function RedirectToForbidden(req: NextRequest): NextResponse<unknown> {
  const absoluteURL = new URL("/unauthorized", req.url);
  return NextResponse.rewrite(absoluteURL.toString());
}

export default async function middleware(
  req: NextRequest
): Promise<NextResponse<unknown> | undefined> {
  if (protectedRoutes.includes(req.nextUrl.pathname)) {
    const token = req.cookies.get("auth_token")?.value;
    if (token !== undefined) {
      if (!(await validToken(token))) {
        return RedirectToForbidden(req);
      }
    } else {
      return RedirectToForbidden(req);
    }
  }
}
