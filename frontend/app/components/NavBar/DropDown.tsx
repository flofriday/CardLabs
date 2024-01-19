import { deleteCookie } from "cookies-next";
import { useRouter } from "next/navigation";
interface Props {
  className?: string;
}

function onLogout(router: any): void {
  deleteCookie("auth_token");
  deleteCookie("refresh_token");
  router.refresh();
  router.replace("/");
}

export default function DropDown({ className = "" }: Props): JSX.Element {
  const router = useRouter();
  return (
    <div
      className={`fixed right-8 top-14 bg-primary w-56 p-3 rounded-md border-2 ${className}`}
    >
      <button
        className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
        onClick={() => {
          onLogout(router);
        }}
      >
        Logout
      </button>
    </div>
  );
}
