import IconButton, { ButtonIcon } from "../../components/iconButton";
import DropDown from "@/app/components/DropDown";

interface Props {
  save: () => void;
  _delete: () => void;
}

export default function EditorButtons({ save, _delete }: Props): JSX.Element {
  return (
    <div className=" w-full mt-44 h-16 flex p-3 space-x-4 ml-4">
      <IconButton text="Compile" type={ButtonIcon.COMPILE} />
      <IconButton onClick={save} text="Save" type={ButtonIcon.SAVE} />
      <IconButton text="Test Bot" type={ButtonIcon.TEST} />
      <IconButton text="Rank Bot" type={ButtonIcon.RANK} />
      <DropDown
        defaultValue="Current"
        className="w-36"
        customButtonClass="text-text p-2 w-full h-full rounded-full shadow-md text-lg rounded-full outline outline-1  hover:bg-primary"
        values={[]}
      />
      <IconButton onClick={_delete} text="Delete" type={ButtonIcon.DELETE} />
    </div>
  );
}
