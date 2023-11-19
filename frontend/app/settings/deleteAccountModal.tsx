import Modal from "../components/modal";

interface Props {
  onClose: () => void;
}

export default function DeleteAccountModal({ onClose }: Props): JSX.Element {
  return (
    <Modal title="Delete Account" modalClassName="w-3/12" onClose={onClose}>
      <div>
        Do your really want to delete your account?
        <div className="flex justify-around pt-4">
          <button className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg">
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
