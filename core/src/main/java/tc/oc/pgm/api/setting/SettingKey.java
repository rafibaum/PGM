package tc.oc.pgm.api.setting;

import static com.google.common.base.Preconditions.*;
import static tc.oc.pgm.api.setting.SettingValue.*;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.kyori.text.Component;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.bukkit.Material;
import tc.oc.pgm.api.player.MatchPlayer;

/**
 * A toggleable setting with various possible {@link SettingValue}s.
 *
 * @see SettingValue
 */
public enum SettingKey {
  CHAT(
      "chat",
      TextColor.DARK_GREEN,
      Material.BEACON,
      CHAT_TEAM,
      CHAT_GLOBAL,
      CHAT_ADMIN), // Changes the default chat channel
  DEATH(
      Arrays.asList("death", "dms"),
      TextColor.DARK_AQUA,
      Material.BONE,
      DEATH_ALL,
      DEATH_OWN), // Changes which death messages are seen
  PICKER(
      "picker",
      TextColor.DARK_PURPLE,
      Material.LEATHER_HELMET,
      PICKER_AUTO,
      PICKER_ON,
      PICKER_OFF), // Changes when the picker is displayed
  JOIN(
      Arrays.asList("join", "jms"),
      TextColor.GOLD,
      Material.IRON_SWORD,
      JOIN_ON,
      JOIN_OFF), // Changes if join messages are seen
  MESSAGE(
      Arrays.asList("message", "dm"),
      TextColor.BLUE,
      Material.CHEST,
      MESSAGE_ON,
      MESSAGE_OFF), // Changes if direct messages are accepted
  OBSERVERS(
      Arrays.asList("observers", "obs"),
      TextColor.GREEN,
      Material.GLASS,
      OBSERVERS_ON,
      OBSERVERS_OFF) {
    @Override
    public void update(MatchPlayer player) {
      player.resetVisibility();
    }
  }, // Changes if observers are visible
  SOUNDS(
      "sounds",
      TextColor.AQUA,
      Material.JUKEBOX,
      SOUNDS_ALL,
      SOUNDS_DM,
      SOUNDS_NONE), // Changes when sounds are played
  VOTE(
      "vote",
      TextColor.RED,
      Material.BOOK,
      VOTE_ON,
      VOTE_OFF), // Changes if the vote book is shown on cycle
  STATS(
      Collections.singletonList("stats"),
      "match.stats.overall",
      TextColor.YELLOW,
      Material.WATCH,
      STATS_ON,
      STATS_OFF), // Changes if stats are tracked
  ;

  private static final String SETTING_TRANSLATION_KEY = "setting.";

  private final List<String> aliases;
  private final Component displayName;
  private final SettingValue[] values;
  private final Material material;
  private final TranslatableComponent lore;

  SettingKey(String name, TextColor color, Material material, SettingValue... values) {
    this(
        Collections.singletonList(name),
        SETTING_TRANSLATION_KEY + name.toLowerCase(),
        color,
        material,
        values);
  }

  SettingKey(List<String> aliases, TextColor color, Material material, SettingValue... values) {
    this(aliases, SETTING_TRANSLATION_KEY + aliases.get(0).toLowerCase(), color, material, values);
  }

  SettingKey(
      List<String> aliases,
      String displayNameKey,
      TextColor color,
      Material material,
      SettingValue... values) {
    checkArgument(!aliases.isEmpty(), "aliases is empty");
    this.aliases = ImmutableList.copyOf(aliases);
    this.displayName = TranslatableComponent.of(displayNameKey, color, TextDecoration.BOLD);
    this.values = values;
    this.material = material;
    this.lore =
        TranslatableComponent.of(
                SETTING_TRANSLATION_KEY + this.getName().toLowerCase() + ".description")
            .color(TextColor.GRAY);
  }

  /**
   * Get the name of the {@link SettingKey}.
   *
   * @return The name.
   */
  public String getName() {
    return aliases.get(0);
  }

  /**
   * Get the display name of the {@link SettingKey}.
   *
   * @return The formatted display name.
   */
  public Component getDisplayName() {
    return displayName;
  }

  /**
   * Gets the description of the setting.
   *
   * @param value the formatted value the setting is currently set to
   * @return the description text for the setting
   */
  public Component getDescription(Component value) {
    return lore.args(value);
  }

  /**
   * Gets the icon used to represent the setting.
   *
   * @return the icon used to represent the setting.
   */
  public Material getIcon() {
    return material;
  }

  /**
   * Get all aliases of this {@link SettingKey}. First index is always equal to {@code #getName}.
   *
   * @return An immutable list of all aliases. Never {@code null} or empty.
   */
  public List<String> getAliases() {
    return aliases;
  }

  /**
   * Get a list of the possible {@link SettingValue}s.
   *
   * @return A array of {@link SettingValue}s, sorted by defined order.
   */
  public SettingValue[] getPossibleValues() {
    return values;
  }

  /**
   * Get the default {@link SettingValue}, which should always be defined first.
   *
   * @return The default {@link SettingValue}.
   */
  public SettingValue getDefaultValue() {
    return getPossibleValues()[0];
  }

  @Override
  public String toString() {
    return getName();
  }

  /**
   * Called whether setting has changed and is ready to be updated internally.
   *
   * @param player owner of the setting
   */
  public void update(MatchPlayer player) {}
}
