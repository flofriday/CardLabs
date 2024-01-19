import { validToken } from "./app/services/AuthenticationService";
import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

// protected sites
const protectedRoutes = ["/settings", "/dashboard", "/bot", "/gameDetail"];


function RedirectToForbidden(req: NextRequest): NextResponse<unknown> {
  const absoluteURL = new URL("/unauthorized", req.url);
  return NextResponse.rewrite(absoluteURL.toString());
}

function startsWithProtectedRoute(str: string) {
  return protectedRoutes.some((substr) =>
    str.toLowerCase().startsWith(substr.toLowerCase())
  );
}

export default async function middleware(
  req: NextRequest
): Promise<NextResponse<unknown> | undefined> {
  if (startsWithProtectedRoute(req.nextUrl.pathname)) {
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
