import { defineStore } from "pinia";
import { computed, ref } from "vue";
import {
  guestLogin as apiGuestLogin,
  login as apiLogin,
  me,
  register as apiRegister,
} from "~/api/auth";
import type { User } from "~/api/types";
import { useRouter } from "vue-router";

export const useAuthStore = defineStore("auth", () => {
  const router = useRouter();

  const token = ref<string | null>(localStorage.getItem("access_token"));
  const user = ref<User | null>(null);
  const loading = ref(false);

  const isLoggedIn = computed(() => !!token.value);
  const isGuest = computed(
    () =>
      !!user.value?.is_guest ||
      !!user.value?.guest ||
      (!!user.value?.id && user.value.id.startsWith("guest-")),
  );

  async function login(name: string, password: string) {
    loading.value = true;
    try {
      const resp = await apiLogin("local", name, password);
      token.value = resp.token;
      localStorage.setItem("access_token", resp.token);
      await fetchUser();
      router.push("/");
    } finally {
      loading.value = false;
    }
  }

  async function loginAsGuest() {
    loading.value = true;
    try {
      const resp = await apiGuestLogin();
      token.value = resp.token;
      localStorage.setItem("access_token", resp.token);
      await fetchUser();
      router.push("/");
    } finally {
      loading.value = false;
    }
  }

  async function register(
    name: string,
    password: string,
    displayName?: string,
  ) {
    loading.value = true;
    try {
      await apiRegister({
        name,
        password,
        display_name: displayName,
      });
      router.push("/login");
    } finally {
      loading.value = false;
    }
  }

  async function fetchUser() {
    if (!token.value) return;
    try {
      user.value = await me();
    } catch {
      logout();
    }
  }

  function logout() {
    token.value = null;
    user.value = null;
    localStorage.removeItem("access_token");
    router.push("/login");
  }

  return {
    token,
    user,
    loading,
    isLoggedIn,
    isGuest,
    login,
    loginAsGuest,
    register,
    fetchUser,
    logout,
  };
});
