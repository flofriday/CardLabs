import Link from "next/link";
import LeftPageHeader from "../components/leftPageHeader";
export default function Login(): JSX.Element {
  return (
    <div className="w-full h-full">
      <LeftPageHeader title="Login" />
      <div className="w-full h-full flex justify-center">
        <div className="h-full flex items-center justify-center w-1/4 flex-col space-y-5">
          <a
            className="btn bg-primary flex justify-center items-center h-20 py-2 w-full font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
            href="/api/oauth2/authorization/github"
          >
            GitHub
          </a>
          <a
            className="btn bg-primary flex justify-center items-center h-20 py-2 w-full font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
            href="/api/oauth2/authorization/google"
          >
            Google
          </a>
        </div>
      </div>
    </div>
  );
}
