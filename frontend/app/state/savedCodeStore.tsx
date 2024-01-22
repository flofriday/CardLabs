import { create } from "zustand";

export const useSaveCodeStore = create((set) => ({
  codeSaved: true,
  setCodeSaved: (isCodeSaved: boolean) => {
    set({ codeSaved: isCodeSaved });
  },
}));
