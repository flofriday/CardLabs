import Link from "next/link";
import { isAuthenticated } from "./services/AuthenticationService";

export default async function NotFoundPage(): Promise<JSX.Element> {
  return (
    <div className="h-full w-full flex justify-center items-center flex-col space-y-6">
      <h1 className="text-8xl font-bold">DON'T PANIC</h1>
      <h1 className="text-8xl font-bold">404</h1>
      <h1 className="text-xl">Just grab your towel, we will guide you back</h1>
      <Link
        href={(await isAuthenticated()) ? "/dashboard" : "/"}
        className="bg-primary rounded-xl p-4 text-xl font-bold"
      >
        <div>Take me Home</div>
      </Link>
    </div>
  );
}
