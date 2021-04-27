package io.github.vampirestudios.raa_materials.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BlocksHelper {
	private static final Map<Block, Integer> COLOR_BY_BLOCK = Maps.newHashMap();

	public static final int FLAG_UPDATE_BLOCK = 1;
	public static final int FLAG_SEND_CLIENT_CHANGES = 2;
	public static final int FLAG_NO_RERENDER = 4;
	public static final int FORSE_RERENDER = 8;
	public static final int FLAG_IGNORE_OBSERVERS = 16;

	public static final int SET_SILENT = FLAG_UPDATE_BLOCK | FLAG_IGNORE_OBSERVERS | FLAG_SEND_CLIENT_CHANGES;
	public static final int SET_OBSERV = FLAG_UPDATE_BLOCK | FLAG_SEND_CLIENT_CHANGES;
	public static final Direction[] HORIZONTAL = makeHorizontal();
	public static final Direction[] DIRECTIONS = Direction.values();

	private static final BlockPos.Mutable POS = new BlockPos.Mutable();
	protected static final BlockState AIR = Blocks.AIR.getDefaultState();
	protected static final BlockState WATER = Blocks.WATER.getDefaultState();

	private static final Vec3i[] OFFSETS = new Vec3i[] {
			new Vec3i(-1, -1, -1), new Vec3i(-1, -1, 0), new Vec3i(-1, -1, 1),
			new Vec3i(-1, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(-1, 0, 1),
			new Vec3i(-1, 1, -1), new Vec3i(-1, 1, 0), new Vec3i(-1, 1, 1),

			new Vec3i(0, -1, -1), new Vec3i(0, -1, 0), new Vec3i(0, -1, 1),
			new Vec3i(0, 0, -1), new Vec3i(0, 0, 0), new Vec3i(0, 0, 1),
			new Vec3i(0, 1, -1), new Vec3i(0, 1, 0), new Vec3i(0, 1, 1),

			new Vec3i(1, -1, -1), new Vec3i(1, -1, 0), new Vec3i(1, -1, 1),
			new Vec3i(1, 0, -1), new Vec3i(1, 0, 0), new Vec3i(1, 0, 1),
			new Vec3i(1, 1, -1), new Vec3i(1, 1, 0), new Vec3i(1, 1, 1)
	};

	public static void addBlockColor(Block block, int color) {
		COLOR_BY_BLOCK.put(block, color);
	}

	public static int getBlockColor(Block block) {
		return COLOR_BY_BLOCK.getOrDefault(block, 0xFF000000);
	}

	public static void setWithoutUpdate(StructureWorldAccess world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, SET_SILENT);
	}

	public static void setWithoutUpdate(StructureWorldAccess world, BlockPos pos, Block block) {
		world.setBlockState(pos, block.getDefaultState(), SET_SILENT);
	}

	public static void setWithUpdate(StructureWorldAccess world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, SET_OBSERV);
	}

	public static void setWithUpdate(StructureWorldAccess world, BlockPos pos, Block block) {
		world.setBlockState(pos, block.getDefaultState(), SET_OBSERV);
	}

	public static int upRay(StructureWorldAccess world, BlockPos pos, int maxDist) {
		int length = 0;
		for (int j = 1; j < maxDist && (world.isAir(pos.up(j))); j++)
			length++;
		return length;
	}

	public static int downRay(StructureWorldAccess world, BlockPos pos, int maxDist) {
		int length = 0;
		for (int j = 1; j < maxDist && (world.isAir(pos.up(j))); j++)
			length++;
		return length;
	}

	public static int downRayRep(StructureWorldAccess world, BlockPos pos, int maxDist) {
		POS.set(pos);
		for (int j = 1; j < maxDist && (world.getBlockState(POS)).getMaterial().isReplaceable(); j++)
		{
			POS.setY(POS.getY() - 1);
		}
		return pos.getY() - POS.getY();
	}

	public static int raycastSqr(StructureWorldAccess world, BlockPos pos, int dx, int dy, int dz, int maxDist) {
		POS.set(pos);
		for (int j = 1; j < maxDist && (world.getBlockState(POS)).getMaterial().isReplaceable(); j++)
		{
			POS.move(dx, dy, dz);
		}
		return (int) pos.getSquaredDistance(POS);
	}

	public static BlockState rotateHorizontal(BlockState state, BlockRotation rotation, Property<Direction> facing) {
		return state.with(facing, rotation.rotate(state.get(facing)));
	}

	public static BlockState mirrorHorizontal(BlockState state, BlockMirror mirror, Property<Direction> facing) {
		return state.rotate(mirror.getRotation(state.get(facing)));
	}

	public static int getLengthDown(StructureWorldAccess world, BlockPos pos, Block block) {
		int count = 1;
		while (world.getBlockState(pos.down(count)).getBlock() == block)
			count++;
		return count;
	}

	public static void cover(StructureWorldAccess world, BlockPos center, Block ground, BlockState cover, int radius, Random random) {
		HashSet<BlockPos> points = new HashSet<BlockPos>();
		HashSet<BlockPos> points2 = new HashSet<BlockPos>();
		if (world.getBlockState(center).getBlock() == ground) {
			points.add(center);
			points2.add(center);
			for (int i = 0; i < radius; i++) {
				for (BlockPos pos : points) {
					for (Vec3i offset : OFFSETS) {
						if (random.nextBoolean()) {
							BlockPos pos2 = pos.add(offset);
							if (random.nextBoolean() && world.getBlockState(pos2).getBlock() == ground
									&& !points.contains(pos2))
								points2.add(pos2);
						}
					}
				}
				points.addAll(points2);
				points2.clear();
			}
			for (BlockPos pos : points) {
				BlocksHelper.setWithoutUpdate(world, pos, cover);
			}
		}
	}

	public static void fixBlocks(StructureWorldAccess world, BlockPos start, BlockPos end) {
		BlockState state;
		Set<BlockPos> doubleCheck = Sets.newHashSet();
		for (int x = start.getX(); x <= end.getX(); x++) {
			POS.setX(x);
			for (int z = start.getZ(); z <= end.getZ(); z++) {
				POS.setZ(z);
				for (int y = start.getY(); y <= end.getY(); y++) {
					POS.setY(y);
					state = world.getBlockState(POS);

					// Liquids
					if (!state.getFluidState().isEmpty()) {
						if (!state.canPlaceAt(world, POS)) {
							setWithoutUpdate(world, POS, WATER);
							POS.setY(POS.getY() - 1);
							state = world.getBlockState(POS);
							while (!state.canPlaceAt(world, POS)) {
								state = state.getFluidState().isEmpty() ? AIR : WATER;
								setWithoutUpdate(world, POS, state);
								POS.setY(POS.getY() - 1);
								state = world.getBlockState(POS);
							}
						}
						POS.setY(y - 1);
						if (world.isAir(POS)) {
							POS.setY(y);
							while (!world.getFluidState(POS).isEmpty()) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
							}
							continue;
						}
						for (Direction dir : HORIZONTAL) {
							if (world.isAir(POS.move(dir))) {
								world.getFluidTickScheduler().schedule(POS, state.getFluidState().getFluid(), 0);
								break;
							}
						}
					}
					/*else if (state.isOf(EndBlocks.SMARAGDANT_CRYSTAL)) {
						POS.setY(POS.getY() - 1);
						if (world.isAir(POS)) {
							POS.setY(POS.getY() + 1);
							while (state.isOf(EndBlocks.SMARAGDANT_CRYSTAL)) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
								state = world.getBlockState(POS);
							}
						}
					}*/
					else if (!state.canPlaceAt(world, POS)) {
						// Chorus
						if (state.isOf(Blocks.CHORUS_PLANT)) {
							Set<BlockPos> ends = Sets.newHashSet();
							Set<BlockPos> add = Sets.newHashSet();
							ends.add(POS.toImmutable());

							for (int i = 0; i < 64 && !ends.isEmpty(); i++) {
								ends.forEach((pos) -> {
									setWithoutUpdate(world, pos, AIR);
									for (Direction dir : HORIZONTAL) {
										BlockPos p = pos.offset(dir);
										BlockState st = world.getBlockState(p);
										if ((st.isOf(Blocks.CHORUS_PLANT) || st.isOf(Blocks.CHORUS_FLOWER)) && !st.canPlaceAt(world, p)) {
											add.add(p);
										}
									}
									BlockPos p = pos.up();
									BlockState st = world.getBlockState(p);
									if ((st.isOf(Blocks.CHORUS_PLANT) || st.isOf(Blocks.CHORUS_FLOWER)) && !st.canPlaceAt(world, p)) {
										add.add(p);
									}
								});
								ends.clear();
								ends.addAll(add);
								add.clear();
							}
						}
						// Vines
						else if (state.getBlock() instanceof VineBlock) {
							while (world.getBlockState(POS).getBlock() instanceof VineBlock) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() - 1);
							}
						}
						// Falling blocks
						else if (state.getBlock() instanceof FallingBlock) {
							BlockState falling = state;

							POS.setY(POS.getY() - 1);
							state = world.getBlockState(POS);

							int ray = downRayRep(world, POS.toImmutable(), 64);
							if (ray > 32) {
								BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE.getDefaultState());
								if (world.getRandom().nextBoolean()) {
									POS.setY(POS.getY() - 1);
									state = world.getBlockState(POS);
									BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE.getDefaultState());
								}
							}
							else {
								POS.setY(y);
								BlockState replacement = AIR;
								for (Direction dir : HORIZONTAL) {
									state = world.getBlockState(POS.offset(dir));
									if (!state.getFluidState().isEmpty()) {
										replacement = state;
										break;
									}
								}
								BlocksHelper.setWithoutUpdate(world, POS, replacement);
								POS.setY(y - ray);
								BlocksHelper.setWithoutUpdate(world, POS, falling);
							}
						}
						// Blocks without support
						else {
							// Double plants
							if (state.getBlock() instanceof TallPlantBlock) {
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
							}
							// Other blocks
							else {
								BlocksHelper.setWithoutUpdate(world, POS, getAirOrFluid(state));
							}
						}
					}
				}
			}
		}

		doubleCheck.forEach((pos) -> {
			if (!world.getBlockState(pos).canPlaceAt(world, pos)) {
				BlocksHelper.setWithoutUpdate(world, pos, AIR);
			}
		});
	}

	private static BlockState getAirOrFluid(BlockState state) {
		return state.getFluidState().isEmpty() ? AIR : state.getFluidState().getBlockState();
	}

	public static Direction[] makeHorizontal() {
		return new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
	}

	public static Direction randomHorizontal(Random random) {
		return HORIZONTAL[random.nextInt(4)];
	}

	public static Direction randomDirection(Random random) {
		return DIRECTIONS[random.nextInt(6)];
	}

	public static boolean isFluid(BlockState blockState) {
		return !blockState.getFluidState().isEmpty();
	}
}