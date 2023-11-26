import Link from "next/link";

export default function SignInUp(): JSX.Element {
  return (
    <div className="flex space-x-6">
      <Link href="/register">
        <button className="btn bg-secondary hover:bg-accent text-text py-2 px-6 rounded-full shadow-md text-lg">
          Register
        </button>
      </Link>
      <Link href="/login">
        <button className="btn bg-secondary hover:bg-accent text-text py-2 px-6 rounded-full shadow-md text-lg">
          Login
        </button>
      </Link>
    </div>
  );
}
