import Modal from "../components/modal";
import { deleteCookie } from "cookies-next";
import { deleteUser } from "../services/UserService";
import { useRouter } from "next/navigation";
import { authenticationStore } from "@/app/state/authenticationStore";

interface Props {
  onClose: () => void;
}

export default function DeleteAccountModal({ onClose }: Props): JSX.Element {
  const router = useRouter();

  const setAuthenticated = authenticationStore(
    (state: any) => state.setAuthenticated
  );

  const deleteUserHandler = (): void => {
    setAuthenticated(false);
    deleteUser()
      .then((res) => {
        if (res) {
          deleteCookie("auth_token");
          localStorage.removeItem("auth_token_expire");
          localStorage.removeItem("refresh_token");
          router.refresh();
          router.replace("/");
        }
      })
      .catch(() => {});
  };

  return (
    <Modal title="Delete Account" modalClassName="w-3/12" onClose={onClose}>
      <div>
        Do your really want to delete your account?
        <div className="flex justify-around pt-4">
          <button
            className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
            onClick={deleteUserHandler}
          >
            Delete Account
          </button>
          <button
            className="btn bg-secondary text-text py-2 w-48 rounded-lg shadow-md text-lg"
            onClick={onClose}
          >
            Go back
          </button>
        </div>
      </div>
    </Modal>
  );
}
