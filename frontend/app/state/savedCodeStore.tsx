import { create } from "zustand";

export const useSaveCodeStore = create((set) => ({
  codeSaved: false,
  setCodeSaved: (isCodeSaved: boolean) => {
    set({ codeSaved: isCodeSaved });
  },
}));
