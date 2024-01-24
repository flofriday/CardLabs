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
    if (token === undefined || !(await validToken(token))) {
      return RedirectToForbidden(req);
    }
  }
  const destination = process.env.MANAGEMENT_HOST ?? "http://localhost:8080";

  if (req.nextUrl.pathname.startsWith('/api')) {
    const path = req.nextUrl.pathname.replace("/api", "")
    const url = new URL(path, destination)

    req.nextUrl.searchParams.forEach((value, key) => {
      url.searchParams.set(key, value);
    });

    return NextResponse.rewrite(url.toString());
  }
  if (req.nextUrl.pathname.startsWith('/login/oauth2/code')) {
    const path = req.nextUrl.pathname
    const url = new URL(path, destination)

    req.nextUrl.searchParams.forEach((value, key) => {
      url.searchParams.set(key, value);
    });

    return NextResponse.rewrite(url.toString());
  }
}
