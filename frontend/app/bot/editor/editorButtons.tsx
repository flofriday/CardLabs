import IconButton, { ButtonIcon } from "../../components/iconButton";
import DropDown from "@/app/components/DropDown";
import Modal from "@/app/components/modal";
import { useState } from "react";

interface Props {
  save: () => void;
  rank: () => void;
  test: () => void;
  _delete: () => void;
}

export default function EditorButtons({
  save,
  rank,
  test,
  _delete,
}: Props): JSX.Element {
  const [deleteModalVisability, setDeleteModalVisability] = useState(false);

  return (
    <div className=" w-full mt-44 h-16 flex p-3 space-x-4 ml-4">
      {deleteModalVisability && (
        <Modal
          title="Delete Bot"
          modalClassName="w-3/12"
          onClose={() => {
            setDeleteModalVisability(false);
          }}
        >
          <div>
            Do you really want to delete your bot?
            <div className="flex justify-around pt-4">
              <button
                className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
                onClick={_delete}
                id="delete_modal_delete_button"
              >
                Delete Bot
              </button>
              <button
                className="btn bg-secondary text-text py-2 w-48 rounded-lg shadow-md text-lg"
                id="delete_modal_back_button"
                onClick={() => {
                  setDeleteModalVisability(false);
                }}
              >
                Go back
              </button>
            </div>
          </div>
        </Modal>
      )}
      <IconButton
        onClick={save}
        text="Save"
        type={ButtonIcon.SAVE}
        id="button_save_bot"
      />
      <IconButton
          text="Test Bot"
          type={ButtonIcon.TEST}
          id="button_test_bot"
          onClick={test}
      />
      <IconButton
        text="Rank Bot"
        type={ButtonIcon.RANK}
        id="button_rank_bot"
        onClick={rank}
      />
      <DropDown
        defaultValue="Current"
        className="w-36"
        customButtonClass="text-text p-2 w-full h-full rounded-full shadow-md text-lg rounded-full outline outline-1  hover:bg-primary"
        values={[]}
      />
      <IconButton
        onClick={() => {
          setDeleteModalVisability(true);
        }}
        text="Delete"
        type={ButtonIcon.DELETE}
        id="button_delete_bot"
      />
    </div>
  );
}
