import IconButton from "./iconButton";
import DropDown from "@/app/components/DropDown";
export enum ButtonIcon {
  COMPILE,
  SAVE,
  TEST,
  RANK,
  DELETE,
}

export default function EditorButtons(): JSX.Element {
  return (
    <div className=" w-full mt-44 h-16 flex p-3">
      <IconButton text="Compile" />
      <IconButton text="Save" />
      <IconButton text="Test Bot" />
      <IconButton text="Rank Bot" />
      <DropDown
        defaultValue="test"
        className="w-36"
        customButtonClass="text-text p-2 w-full h-full rounded-full shadow-md text-lg rounded-full outline outline-1"
        values={["test"]}
      />
      <IconButton text="Delete" />
    </div>
  );
}
