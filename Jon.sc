Jon {
	classvar <nperiods = 3, <buffer_size = 256, <samplerate = 48000;
	classvar <playback_device, <capture_device, <audio_device;
	classvar <launch_qjackctl = false;
	classvar <command_string;
	classvar make_command_func;

	*initClass {
		var sub_func = {
			command_string = "jon";

			command_string = command_string
			++ format(" -n % -p % -r %",
				nperiods, buffer_size, samplerate
			);

			if (audio_device.notNil) {
				command_string = command_string
				++ " -d "++audio_device;
			};

			if (playback_device.notNil && (playback_device != audio_device)) {
				command_string = command_string
				++ " -b "++playback_device;
			};

			if (capture_device.notNil && (capture_device != audio_device)) {
				command_string = command_string
				++ " -c "++capture_device;
			};

			if (launch_qjackctl) {
				command_string = command_string ++ " -q ";
			};
		};

		make_command_func = {
			if (this.isInstalled) {
				sub_func.value;
				make_command_func = sub_func;
			} /* else */ {
				"Warning: jon not installed".postln;
			}
		}
	}

	*pr_make_command  {
		make_command_func.value;
	}

	*nperiods_{ | value(3) |
		nperiods = value;
		this.pr_make_command;
	}

	*buffer_size_{ | value(256) |
		buffer_size = value;
		this.pr_make_command;
	}

	*samplerate_{ | value(48e3) |
		samplerate = value;
		this.pr_make_command;
	}

	*audio_device_{ | new_device |
		audio_device = new_device;
		this.pr_make_command;
	}

	*playback_device_{ | new_device |
		playback_device = new_device;
		this.pr_make_command;
	}

	*capture_device_{ | new_device |
		capture_device = new_device;
		this.pr_make_command;
	}

	*launch_qjackctl_{ | bool(false) |
		launch_qjackctl = bool;
		this.pr_make_command;
	}

	*isInstalled {
		^this.version.notEmpty;
	}

	*start {
		if (command_string.isNil) {
			this.pr_make_command;
		};

		^command_string.unixCmdGetStdOut;
	}

	*kill {
		^"jon -k".unixCmdGetStdOut;
	}

	*version {
		^"jon -v".unixCmdGetStdOut;
	}
}