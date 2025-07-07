<script lang="ts">
	import { _ } from 'svelte-i18n';
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { dev } from '$app/environment';
	import { ProgressRing } from '@skeletonlabs/skeleton-svelte';
	import { goto } from '$app/navigation';

	let { data } = $props();
	let isLoading = $state(false);
	let showSuccess = $state(false);

	const returnToLogin = () => {
		goto('/login');
	};

	const { form, errors, constraints, enhance } = superForm(data.form, {
		onSubmit: () => {
			isLoading = true;
		},
		onUpdate: () => {
			isLoading = false;
			showSuccess = true;
			toaster.create({
				description: $_('passwordForgot.emailSent'),
				type: 'success'
			});
		},
		onError: (error) => {
			isLoading = false;
			console.error('Error:', error.result.error);
			toaster.create({
				description: $_('error.description', { values: { status: error.result.status } }),
				type: 'error'
			});
		}
	});
</script>

<main class="flex h-full flex-col items-center justify-center gap-4">
	<form
		method="POST"
		use:enhance
		action="?/forgot-password"
		class="card preset-filled-surface-100-900 w-full max-w-lg space-y-8 p-8"
	>
		<h1 class="preset-typo-headline">{$_('passwordForgot.title')}</h1>

		<div class="space-y-4">
			<label class="label">
				<span class="label-text">{$_('common.email')}</span>
				<input
					class="input preset-filled-surface-200-800"
					name="username"
					type="text"
					placeholder={$_('common.email')}
					aria-invalid={$errors.username ? 'true' : undefined}
					bind:value={$form.username}
					{...$constraints.username}
				/>
				{#if $errors.username}<span class="text-error-500">{$errors.username}</span>{/if}
			</label>
		</div>
		<div class="flex gap-2">
			<button type="button" class="btn preset-outlined-primary-500 ml-auto" onclick={returnToLogin}>
				{$_('passwordForgot.returnToLogin')}
			</button>
			<button type="submit" class="btn preset-filled-primary-500" disabled={isLoading}>
				{#if isLoading}
					<ProgressRing
						value={null}
						size="size-4"
						meterStroke="stroke-primary-500"
						trackStroke="stroke-primary-50-950"
					/>
					{$_('passwordForgot.sending')}
				{:else}
					{$_('passwordForgot.reset')}
				{/if}
			</button>
		</div>
	</form>
	{#if showSuccess}
		<p class="text-success-500">
			{$_('passwordForgot.emailSent')}
		</p>
	{/if}
	<SuperDebug data={$form} display={dev} />
</main>
